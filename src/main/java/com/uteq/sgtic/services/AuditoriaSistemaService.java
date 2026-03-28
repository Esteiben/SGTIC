package com.uteq.sgtic.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteq.sgtic.entities.AuditoriaSistema;
import com.uteq.sgtic.repository.AuditoriaSistemaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuditoriaSistemaService {

    @Autowired
    private AuditoriaSistemaRepository auditoriaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Map<Integer, String>> cacheDiccionarios = new HashMap<>();
    private Map<String, String[]> reglasTraduccion = new HashMap<>();

    @PostConstruct
    public void inicializarReglas() {

        reglasTraduccion.put("id_usuario", new String[]{"usuario", "id_usuario", "COALESCE(nombres, '') || ' ' || COALESCE(apellidos, '')"});
        reglasTraduccion.put("id_carrera", new String[]{"carrera", "id_carrera", "nombre"});
        reglasTraduccion.put("id_facultad", new String[]{"facultad", "id_facultad", "nombre"});
        reglasTraduccion.put("id_periodo", new String[]{"periodo_academico", "id_periodo", "nombre"});
        reglasTraduccion.put("id_rol", new String[]{"rol", "id_rol", "nombre_mostrar"});
        reglasTraduccion.put("id_comision", new String[]{"comision", "id_comision", "nombre"});
        reglasTraduccion.put("id_opcion", new String[]{"opcion_titulacion", "id_opcion", "nombre"});
        reglasTraduccion.put("id_estudiante", new String[]{"usuario", "id_usuario", "COALESCE(nombres, '') || ' ' || COALESCE(apellidos, '')"});
        reglasTraduccion.put("id_docente", new String[]{"usuario", "id_usuario", "COALESCE(nombres, '') || ' ' || COALESCE(apellidos, '')"});
    }

    public List<AuditoriaSistema> obtenerHistorialAuditoria() {
        List<AuditoriaSistema> historial = auditoriaRepository.findAllByOrderByFechaHoraDesc();

        cacheDiccionarios.clear();

        for (AuditoriaSistema auditoria : historial) {
            if (auditoria.getValoresAnteriores() != null) {
                auditoria.setValoresAnteriores(procesarJson(auditoria.getValoresAnteriores()));
            }
            if (auditoria.getValoresNuevos() != null) {
                auditoria.setValoresNuevos(procesarJson(auditoria.getValoresNuevos()));
            }
        }
        return historial;
    }

    private String procesarJson(String jsonRaw) {
        try {
            Map<String, Object> mapOriginal = objectMapper.readValue(jsonRaw, new TypeReference<Map<String, Object>>() {});
            Map<String, Object> mapTraducido = new HashMap<>();

            for (Map.Entry<String, Object> entry : mapOriginal.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (reglasTraduccion.containsKey(key) && value instanceof Number) {
                    Integer id = ((Number) value).intValue();
                    String[] regla = reglasTraduccion.get(key);

                    String tabla = regla[0];
                    String colId = regla[1];
                    String colMostrar = regla[2];

                    Map<Integer, String> diccionarioTabla = obtenerDiccionario(tabla, colId, colMostrar);

                    value = diccionarioTabla.getOrDefault(id, "ID: " + id);
                }

                mapTraducido.put(formatearLlave(key), value);
            }
            return objectMapper.writeValueAsString(mapTraducido);
        } catch (Exception e) {
            return jsonRaw;
        }
    }

    private Map<Integer, String> obtenerDiccionario(String tabla, String colId, String colMostrar) {
        if (cacheDiccionarios.containsKey(tabla)) {
            return cacheDiccionarios.get(tabla);
        }

        String sql = "SELECT " + colId + " AS id, " + colMostrar + " AS valor FROM " + tabla;
        Map<Integer, String> mapa = jdbcTemplate.query(sql, rs -> {
            Map<Integer, String> m = new HashMap<>();
            while (rs.next()) {
                m.put(rs.getInt("id"), rs.getString("valor"));
            }
            return m;
        });

        cacheDiccionarios.put(tabla, mapa);
        return mapa;
    }

    private String formatearLlave(String key) {
        if (key.startsWith("id_")) key = key.substring(3);
        String[] words = key.split("_");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        return sb.toString().trim();
    }
}