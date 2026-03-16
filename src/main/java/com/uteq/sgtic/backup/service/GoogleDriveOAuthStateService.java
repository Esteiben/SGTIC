package com.uteq.sgtic.backup.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GoogleDriveOAuthStateService {

    private final Map<String, OAuthStatePayload> stateStore = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    public String createState(Integer userId) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String state = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        stateStore.put(state, new OAuthStatePayload(userId, LocalDateTime.now().plusMinutes(10)));

        return state;
    }

    public Integer consumeState(String state) {
        OAuthStatePayload payload = stateStore.remove(state);

        if (payload == null) {
            throw new IllegalStateException("El state OAuth no existe o ya fue usado.");
        }

        if (payload.expiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("El state OAuth expiró.");
        }

        return payload.userId();
    }

    private record OAuthStatePayload(Integer userId, LocalDateTime expiresAt) {
    }
}