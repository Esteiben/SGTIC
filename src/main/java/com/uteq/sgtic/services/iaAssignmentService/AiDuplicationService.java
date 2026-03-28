package com.uteq.sgtic.services.iaAssignmentService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.Loader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class AiDuplicationService {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.ai.openai.api-key}")
    private String groqApiKey;

    @Value("${spring.ai.openai.chat.options.model}")
    private String groqModel;

    @Value("${spring.ai.openai.base-url}")
    private String groqBaseUrl;

    public AiDuplicationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public String verificarDuplicidad(Integer idTemaPropuestoEvaluar) {
        try {
            String sqlActual = "SELECT titulo, descripcion, url_documento FROM public.tema_propuesto_estudiante WHERE id_tema_propuesto = ?";
            Map<String, Object> propuestaActual = jdbcTemplate.queryForMap(sqlActual, idTemaPropuestoEvaluar);

            String tituloActual = (String) propuestaActual.get("titulo");
            String descActual = (String) propuestaActual.get("descripcion");
            String urlPdf = (String) propuestaActual.get("url_documento");
            String textoPdf = extraerTextoDePdf(urlPdf, 3);

            String sqlPropuestas = "SELECT * FROM public.fn_obtener_propuestas_carrera_para_ia(?)";
            List<Map<String, Object>> otrasPropuestas = jdbcTemplate.queryForList(sqlPropuestas, idTemaPropuestoEvaluar);

            String sqlBanco = "SELECT * FROM public.fn_obtener_banco_temas_carrera_para_ia(?)";
            List<Map<String, Object>> bancoTemas = jdbcTemplate.queryForList(sqlBanco, idTemaPropuestoEvaluar);

            return llamarAGroq(tituloActual, descActual, textoPdf, otrasPropuestas, bancoTemas);

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Ocurrió un error al procesar la verificación: " + e.getMessage() + "\"}";
        }
    }

    private String extraerTextoDePdf(String urlString, int paginasMaximas) {
        if (urlString == null || urlString.isEmpty()) return "Sin documento anexo.";

        try (InputStream in = URI.create(urlString).toURL().openStream();
             PDDocument document = Loader.loadPDF(in.readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(1);
            stripper.setEndPage(paginasMaximas);
            return stripper.getText(document);

        } catch (Exception e) {
            System.out.println("No se pudo leer el PDF: " + urlString);
            return "No se pudo extraer el texto del documento.";
        }
    }

    private String llamarAGroq(String titulo, String descripcion, String pdfText,
                               List<Map<String, Object>> propuestas, List<Map<String, Object>> banco) throws Exception {

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("model", groqModel);
        payload.put("temperature", 0.1);

        ArrayNode messages = payload.putArray("messages");

        ObjectNode systemMessage = objectMapper.createObjectNode();
        systemMessage.put("role", "system");
        systemMessage.put("content", "Eres un evaluador académico experto. Tu tarea es analizar una propuesta de titulación y compararla con el historial de la carrera. " +
                "Busca similitudes mayores al 70%. RESPONDE ÚNICAMENTE CON UN JSON VÁLIDO. " +
                "Formato esperado: {\"duplicados_estudiantes\": [{\"id_tema_propuesto\": 0, \"titulo\": \"\", \"similitud\": 0, \"motivo\": \"\"}], \"duplicados_banco\": [{\"id_tema\": 0, \"titulo\": \"\", \"similitud\": 0, \"motivo\": \"\"}]}");
        messages.add(systemMessage);

        String contenidoUsuario = String.format(
                "PROPUESTA A EVALUAR:\n- Título: %s\n- Descripción: %s\n- Extracto Documento: %s\n\n" +
                        "OTRAS PROPUESTAS DE ESTUDIANTES:\n%s\n\n" +
                        "BANCO OFICIAL DE TEMAS:\n%s",
                titulo, descripcion, pdfText, objectMapper.writeValueAsString(propuestas), objectMapper.writeValueAsString(banco)
        );

        ObjectNode userMessage = objectMapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", contenidoUsuario);
        messages.add(userMessage);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(groqBaseUrl + "/chat/completions"))
                .header("Authorization", "Bearer " + groqApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        //  IMPRIMIR LA RESPUESTA REAL DE GROQ EN CONSOLA PARA DEPURAR
        System.out.println("====== RESPUESTA DE GROQ ======");
        System.out.println(responseBody);
        System.out.println("===============================");

        var jsonNode = objectMapper.readTree(responseBody);

        // Si Groq mandó un error (ej. API Key mala), lo detectamos aquí:
        if (jsonNode.has("error")) {
            throw new RuntimeException("Groq rechazó la petición: " + jsonNode.path("error").path("message").asText());
        }

        // Si no hay 'choices', lanzamos un error claro
        var choicesNode = jsonNode.path("choices");
        if (choicesNode.isMissingNode() || !choicesNode.isArray() || choicesNode.size() == 0) {
            throw new RuntimeException("Respuesta incompleta de Groq: " + responseBody);
        }

        // --- SOLUCIÓN APLICADA AQUÍ ---
        // Extraemos el texto crudo del modelo
        String respuestaIA = choicesNode.get(0).path("message").path("content").asText();

        // Limpiamos las molestas etiquetas Markdown si es que existen
        respuestaIA = respuestaIA.replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();

        return respuestaIA;
    }
}