package com.uteq.sgtic.services.iaAssignmentService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteq.sgtic.dtos.iaMatchResultDTO.AiMatchResultDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AiAssignmentService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<AiMatchResultDTO> obtenerSugerencias(String titulo, String descripcion, List<Object> docentes) {
        try {

            String listaDocentes = objectMapper.writeValueAsString(docentes);
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama-3.3-70b-versatile");

            String promptUser = String.format("""
                Proyecto: %s. 
                Descripción: %s.
                Lista de Docentes: %s.
                
                TAREA:
                1. Analiza qué docentes tienen mejor perfil para el proyecto.
                2. Asigna matchScore (0-100) e idDocente.
                3. Devuelve ÚNICAMENTE un arreglo JSON puro, sin markdown, sin texto extra.
                Formato: [{"idDocente": 1, "matchScore": 95, "razonIA": "Experto en el tema"}]
                """, titulo, descripcion, listaDocentes);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", "Eres un coordinador académico. Solo respondes con JSON puro."));
            messages.add(Map.of("role", "user", "content", promptUser));

            requestBody.put("messages", messages);
            requestBody.put("stream", false);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.groq.com/openai/v1/chat/completions", entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            String respuestaIA = root.path("choices").get(0).path("message").path("content").asText();
            respuestaIA = respuestaIA.replace("```json", "").replace("```", "").trim();

            return objectMapper.readValue(respuestaIA, new TypeReference<List<AiMatchResultDTO>>() {});

        } catch (Exception e) {
            System.err.println("🚨 Error IA Groq: " + e.getMessage());
            return List.of();
        }
    }
}