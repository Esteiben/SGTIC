package com.uteq.sgtic.backup.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteq.sgtic.backup.entity.BackupConfig;
import com.uteq.sgtic.backup.entity.BackupDriveAccount;
import com.uteq.sgtic.backup.entity.BackupExecution;
import com.uteq.sgtic.backup.repository.BackupDriveAccountRepository;
import com.uteq.sgtic.backup.repository.BackupExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDriveStorageService {

    private final BackupDriveAccountRepository backupDriveAccountRepository;
    private final BackupExecutionRepository backupExecutionRepository;
    private final GoogleDriveAuthService googleDriveAuthService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(60))
            .build();

    public void uploadBackupIfConfigured(BackupConfig config, BackupExecution execution, Path backupFile) {
        if (config.getDriveEnabled() == null || !config.getDriveEnabled()) {
            execution.setDriveUploaded(false);
            execution.setDriveMessage("Subida a Google Drive desactivada.");
            backupExecutionRepository.saveAndFlush(execution);
            return;
        }

        if (config.getDriveAccountId() == null) {
            execution.setDriveUploaded(false);
            execution.setDriveMessage("Drive está activado, pero no hay cuenta vinculada.");
            backupExecutionRepository.saveAndFlush(execution);
            return;
        }

        if (backupFile == null || !Files.exists(backupFile)) {
            execution.setDriveUploaded(false);
            execution.setDriveMessage("No existe el archivo local para subir a Drive.");
            backupExecutionRepository.saveAndFlush(execution);
            return;
        }

        BackupDriveAccount account = backupDriveAccountRepository.findByIdAndActiveTrue(config.getDriveAccountId())
                .orElseThrow(() -> new IllegalStateException("No existe una cuenta de Drive activa con id " + config.getDriveAccountId()));

        try {
            String accessToken = googleDriveAuthService.refreshAccessToken(account);
            DriveUploadResult result = uploadFile(accessToken, backupFile, config.getDriveFolderId());

            execution.setDriveUploaded(true);
            execution.setDriveFileId(result.fileId());
            execution.setDriveFileName(result.fileName());
            execution.setDriveWebViewLink(result.webViewLink());
            execution.setDriveUploadedAt(LocalDateTime.now());
            execution.setDriveMessage("Archivo subido correctamente a Google Drive.");

            backupExecutionRepository.saveAndFlush(execution);

        } catch (Exception e) {
            log.error("Falló la subida a Google Drive", e);

            execution.setDriveUploaded(false);
            execution.setDriveMessage("Falló la subida a Google Drive: " + truncate(e.getMessage(), 4000));
            backupExecutionRepository.saveAndFlush(execution);
        }
    }

    private DriveUploadResult uploadFile(String accessToken, Path backupFile, String folderId) {
        try {
            String boundary = "====" + UUID.randomUUID() + "====";
            String metadataJson = buildMetadataJson(backupFile, folderId);

            byte[] requestBody = buildMultipartBody(boundary, metadataJson, backupFile);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart&fields=id,name,webViewLink"))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "multipart/related; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("Google Drive devolvió error: " + response.body());
            }

            JsonNode json = objectMapper.readTree(response.body());

            return new DriveUploadResult(
                    json.path("id").asText(null),
                    json.path("name").asText(null),
                    json.path("webViewLink").asText(null)
            );

        } catch (Exception e) {
            throw new IllegalStateException("Error subiendo archivo a Google Drive: " + e.getMessage(), e);
        }
    }

    private String buildMetadataJson(Path backupFile, String folderId) {
        String escapedName = backupFile.getFileName().toString().replace("\"", "\\\"");
        if (StringUtils.hasText(folderId)) {
            return """
                    {
                      "name": "%s",
                      "parents": ["%s"]
                    }
                    """.formatted(escapedName, folderId);
        }

        return """
                {
                  "name": "%s"
                }
                """.formatted(escapedName);
    }

    private byte[] buildMultipartBody(String boundary, String metadataJson, Path backupFile) throws Exception {
        byte[] fileBytes = Files.readAllBytes(backupFile);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);

        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Type: application/json; charset=UTF-8\r\n\r\n");
        writer.append(metadataJson).append("\r\n");

        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Type: application/octet-stream\r\n\r\n");
        writer.flush();

        outputStream.write(fileBytes);
        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));

        writer.append("--").append(boundary).append("--").append("\r\n");
        writer.flush();

        return outputStream.toByteArray();
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

    private record DriveUploadResult(String fileId, String fileName, String webViewLink) {
    }
}