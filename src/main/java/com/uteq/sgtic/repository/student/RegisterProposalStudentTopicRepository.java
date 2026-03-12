package com.uteq.sgtic.repository.student;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RegisterProposalStudentTopicRepository {

    private final JdbcTemplate jdbcTemplate;

    public Integer registrarPropuestaTemaEstudiante(
            Integer idUsuario,
            Integer idOpcion,
            String titulo,
            String descripcion,
            String urlDocumento
    ) {
        String sql = "SELECT public.fn_registrar_propuesta_tema_estudiante(?, ?, ?, ?, ?)";

        return jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                idUsuario,
                idOpcion,
                titulo,
                descripcion,
                urlDocumento
        );
    }
}