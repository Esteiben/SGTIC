package com.uteq.sgtic.backup.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteq.sgtic.backup.dto.DriveAccountResponseDTO;
import com.uteq.sgtic.backup.dto.DriveAuthUrlResponseDTO;
import com.uteq.sgtic.backup.entity.BackupDriveAccount;
import com.uteq.sgtic.backup.repository.BackupDriveAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDriveAuthService {

    private final BackupDriveAccountRepository backupDriveAccountRepository;
    private final GoogleDriveOAuthStateService stateService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${backup.drive.client-id}")
    private String clientId;

    @Value("${backup.drive.client-secret}")
    private String clientSecret;

    @Value("${backup.drive.redirect-uri}")
    private String redirectUri;

    @Value("${backup.drive.scope}")
    private String scope;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public DriveAuthUrlResponseDTO buildAuthorizationUrl(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId es obligatorio.");
        }

        String state = stateService.createState(userId);

        String authorizationUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + enc(clientId)
                + "&redirect_uri=" + enc(redirectUri)
                + "&response_type=code"
                + "&scope=" + enc(scope)
                + "&access_type=offline"
                + "&prompt=consent"
                + "&include_granted_scopes=true"
                + "&state=" + enc(state);

        return new DriveAuthUrlResponseDTO(authorizationUrl);
    }

    public DriveAccountResponseDTO handleCallback(String code, String state) {
        if (!StringUtils.hasText(code)) {
            throw new IllegalArgumentException("No se recibió authorization code.");
        }
        if (!StringUtils.hasText(state)) {
            throw new IllegalArgumentException("No se recibió state.");
        }

        Integer userId = stateService.consumeState(state);
        TokenResponse tokenResponse = exchangeCodeForTokens(code);

        if (!StringUtils.hasText(tokenResponse.refreshToken())) {
            throw new IllegalStateException("Google no devolvió refresh_token. Vuelve a vincular la cuenta con prompt=consent.");
        }

        UserInfo userInfo = fetchUserInfo(tokenResponse.accessToken());

        backupDriveAccountRepository.findFirstByActiveTrue().ifPresent(existing -> {
            existing.setActive(false);
            existing.setUpdatedBy(userId);
            backupDriveAccountRepository.save(existing);
        });

        BackupDriveAccount account = BackupDriveAccount.builder()
                .createdBy(userId)
                .updatedBy(null)
                .googleEmail(userInfo.email())
                .googleSub(userInfo.sub())
                .refreshToken(tokenResponse.refreshToken())
                .scope(tokenResponse.scope())
                .active(true)
                .build();

        account = backupDriveAccountRepository.save(account);
        return mapToResponse(account);
    }

    public DriveAccountResponseDTO getActiveAccount() {
        BackupDriveAccount account = backupDriveAccountRepository.findFirstByActiveTrue()
                .orElseThrow(() -> new IllegalStateException("No hay cuenta de Drive vinculada."));

        return mapToResponse(account);
    }

    public void unlinkAccount(Long accountId, Integer userId) {
        BackupDriveAccount account = backupDriveAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalStateException("No existe la cuenta de Drive con id " + accountId));

        account.setActive(false);
        account.setUpdatedBy(userId);
        backupDriveAccountRepository.save(account);
    }

    public String refreshAccessToken(BackupDriveAccount account) {
        try {
            String form = "client_id=" + enc(clientId)
                    + "&client_secret=" + enc(clientSecret)
                    + "&refresh_token=" + enc(account.getRefreshToken())
                    + "&grant_type=refresh_token";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://oauth2.googleapis.com/token"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("No se pudo refrescar el access token: " + response.body());
            }

            JsonNode json = objectMapper.readTree(response.body());
            return json.path("access_token").asText();

        } catch (Exception e) {
            throw new IllegalStateException("Error refrescando access token de Google Drive: " + e.getMessage(), e);
        }
    }

    private TokenResponse exchangeCodeForTokens(String code) {
        try {
            String form = "code=" + enc(code)
                    + "&client_id=" + enc(clientId)
                    + "&client_secret=" + enc(clientSecret)
                    + "&redirect_uri=" + enc(redirectUri)
                    + "&grant_type=authorization_code";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://oauth2.googleapis.com/token"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("Error intercambiando authorization code: " + response.body());
            }

            JsonNode json = objectMapper.readTree(response.body());

            return new TokenResponse(
                    json.path("access_token").asText(),
                    json.path("refresh_token").asText(null),
                    json.path("scope").asText(null)
            );

        } catch (Exception e) {
            throw new IllegalStateException("Error obteniendo tokens desde Google: " + e.getMessage(), e);
        }
    }

    private UserInfo fetchUserInfo(String accessToken) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://openidconnect.googleapis.com/v1/userinfo"))
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("No se pudo obtener userinfo: " + response.body());
            }

            JsonNode json = objectMapper.readTree(response.body());

            return new UserInfo(
                    json.path("sub").asText(null),
                    json.path("email").asText(null)
            );

        } catch (Exception e) {
            throw new IllegalStateException("Error consultando userinfo de Google: " + e.getMessage(), e);
        }
    }

    private DriveAccountResponseDTO mapToResponse(BackupDriveAccount account) {
        return DriveAccountResponseDTO.builder()
                .id(account.getId())
                .googleEmail(account.getGoogleEmail())
                .googleSub(account.getGoogleSub())
                .active(account.getActive())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    private String enc(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private record TokenResponse(String accessToken, String refreshToken, String scope) {
    }

    private record UserInfo(String sub, String email) {
    }
}