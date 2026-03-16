package com.uteq.sgtic.repository.student;

import com.uteq.sgtic.dtos.student.reports.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentReportRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Optional<StudentAuthContextDTO> findStudentContextByEmail(String email) {
        String sql = """
            SELECT
                e.id_estudiante AS studentId,
                u.id_usuario AS userId,
                u.correo AS email,
                concat_ws(' ', u.nombres, u.apellidos) AS fullName,
                e.id_carrera AS careerId,
                c.nombre AS careerName,
                e.id_periodo AS academicPeriodId,
                p.nombre AS academicPeriodName,
                e.estado_titulacion AS titulationStatus
            FROM estudiante e
            INNER JOIN usuario u ON u.id_usuario = e.id_usuario
            LEFT JOIN carrera c ON c.id_carrera = e.id_carrera
            LEFT JOIN periodo_academico p ON p.id_periodo = e.id_periodo
            WHERE LOWER(u.correo) = LOWER(:email)
            LIMIT 1
        """;

        return queryOne(sql,
                new MapSqlParameterSource("email", email),
                StudentAuthContextDTO.class);
    }

    public Optional<StudentGeneralStatusReportDTO> findGeneralStatusByStudentId(Integer studentId) {
        String sql = """
            SELECT
                e.id_estudiante AS studentId,
                concat_ws(' ', u.nombres, u.apellidos) AS studentFullName,
                u.correo AS studentEmail,
                c.nombre AS careerName,
                pa.nombre AS academicPeriodName,
                e.estado_titulacion AS titulationStatus,
                ot.nombre AS currentTitulationOption,
                eot.fecha_seleccion AS optionSelectionDate,
                ept.tipo_periodo AS titulationPeriodType,
                ept.aprobado AS titulationPeriodApproved,
                ept.fecha_aprobacion AS approvalDate,
                ept.observaciones AS observations
            FROM estudiante e
            INNER JOIN usuario u ON u.id_usuario = e.id_usuario
            LEFT JOIN carrera c ON c.id_carrera = e.id_carrera
            LEFT JOIN periodo_academico pa ON pa.id_periodo = e.id_periodo
            LEFT JOIN LATERAL (
                SELECT x.id_opcion, x.fecha_seleccion
                FROM estudiante_opcion_titulacion x
                WHERE x.id_estudiante = e.id_estudiante
                ORDER BY x.fecha_seleccion DESC NULLS LAST, x.id_opcion DESC
                LIMIT 1
            ) eot ON TRUE
            LEFT JOIN opcion_titulacion ot ON ot.id_opcion = eot.id_opcion
            LEFT JOIN LATERAL (
                SELECT x.tipo_periodo, x.aprobado, x.fecha_aprobacion, x.observaciones
                FROM estudiante_periodo_titulacion x
                WHERE x.id_estudiante = e.id_estudiante
                ORDER BY x.id DESC
                LIMIT 1
            ) ept ON TRUE
            WHERE e.id_estudiante = :studentId
        """;

        return queryOne(sql,
                new MapSqlParameterSource("studentId", studentId),
                StudentGeneralStatusReportDTO.class);
    }

    public Optional<StudentOptionReportDTO> findOptionReportByStudentId(Integer studentId) {
        String sql = """
            SELECT
                e.id_estudiante AS studentId,
                ot.nombre AS currentOptionName,
                ot.descripcion AS currentOptionDescription,
                eot.fecha_seleccion AS selectionDate,
                hco.fecha_cambio AS lastChangeDate,
                ota.nombre AS previousOptionName,
                hco.motivo AS changeReason
            FROM estudiante e
            LEFT JOIN LATERAL (
                SELECT x.id_opcion, x.fecha_seleccion
                FROM estudiante_opcion_titulacion x
                WHERE x.id_estudiante = e.id_estudiante
                ORDER BY x.fecha_seleccion DESC NULLS LAST, x.id_opcion DESC
                LIMIT 1
            ) eot ON TRUE
            LEFT JOIN opcion_titulacion ot ON ot.id_opcion = eot.id_opcion
            LEFT JOIN LATERAL (
                SELECT x.id_opcion_anterior, x.fecha_cambio, x.motivo
                FROM historial_cambio_opcion x
                WHERE x.id_estudiante = e.id_estudiante
                ORDER BY x.fecha_cambio DESC NULLS LAST, x.id DESC
                LIMIT 1
            ) hco ON TRUE
            LEFT JOIN opcion_titulacion ota ON ota.id_opcion = hco.id_opcion_anterior
            WHERE e.id_estudiante = :studentId
        """;

        return queryOne(sql,
                new MapSqlParameterSource("studentId", studentId),
                StudentOptionReportDTO.class);
    }

    public Optional<StudentTopicReportDTO> findCurrentTopicReportByStudentId(Integer studentId) {
        String sql = """
            SELECT
                pt.id_propuesta AS proposalId,
                CASE
                    WHEN pt.id_tema IS NOT NULL THEN 'BANCO_TEMAS'
                    ELSE 'TEMA_PROPUESTO'
                END AS topicSource,
                COALESCE(t.titulo, tp.titulo) AS topicTitle,
                COALESCE(t.descripcion, tp.descripcion) AS topicDescription,
                ot.nombre AS titulationOption,
                pa.nombre AS academicPeriodName,
                pt.fecha_envio AS proposalSubmissionDate,
                pt.estado AS proposalStatus,
                pt.observaciones AS proposalObservations,
                c.nombre AS commissionName,
                COALESCE(tp.estado, pt.estado) AS topicStatus,
                tp.url_documento AS documentUrl,
                tp.feedback_docente AS teacherFeedback,
                tp.numero_version AS versionNumber
            FROM propuesta_trabajo pt
            LEFT JOIN tema t ON t.id_tema = pt.id_tema
            LEFT JOIN comision c ON c.id_comision = t.id_comision
            LEFT JOIN tema_propuesto_estudiante tp ON tp.id_tema_propuesto = pt.id_tema_propuesto
            LEFT JOIN opcion_titulacion ot ON ot.id_opcion = COALESCE(t.id_opcion, tp.id_opcion)
            LEFT JOIN periodo_academico pa ON pa.id_periodo = pt.id_periodo
            WHERE pt.id_estudiante = :studentId
            ORDER BY pt.fecha_envio DESC NULLS LAST, pt.id_propuesta DESC
            LIMIT 1
        """;

        return queryOne(sql,
                new MapSqlParameterSource("studentId", studentId),
                StudentTopicReportDTO.class);
    }

    public List<StudentTopicChangeHistoryItemDTO> findTopicChangeHistoryByStudentId(Integer studentId) {
        String sql = """
            SELECT
                hct.id AS changeId,
                pa.nombre AS academicPeriodName,
                ta.titulo AS previousTopicTitle,
                tn.titulo AS newTopicTitle,
                oa.nombre AS previousOptionName,
                onv.nombre AS newOptionName,
                hct.fecha_cambio AS changeDate,
                hct.motivo AS reason
            FROM historial_cambio_tema hct
            LEFT JOIN periodo_academico pa ON pa.id_periodo = hct.id_periodo
            LEFT JOIN tema ta ON ta.id_tema = hct.id_tema_anterior
            LEFT JOIN tema tn ON tn.id_tema = hct.id_tema_nuevo
            LEFT JOIN opcion_titulacion oa ON oa.id_opcion = hct.id_opcion_anterior
            LEFT JOIN opcion_titulacion onv ON onv.id_opcion = hct.id_opcion_nueva
            WHERE hct.id_estudiante = :studentId
            ORDER BY hct.fecha_cambio DESC NULLS LAST, hct.id DESC
        """;

        return queryList(sql,
                new MapSqlParameterSource("studentId", studentId),
                StudentTopicChangeHistoryItemDTO.class);
    }

    public List<StudentProposalVersionHistoryItemDTO> findProposalVersionHistoryByStudentId(Integer studentId) {
        String sql = """
            SELECT
                hv.id_historial AS historyId,
                hv.id_propuesta AS proposalId,
                hv.id_tema_propuesto AS topicProposedId,
                hv.numero_version AS versionNumber,
                hv.titulo AS title,
                hv.descripcion AS description,
                hv.url_documento AS documentUrl,
                ot.nombre AS titulationOption,
                hv.estado_propuesta AS proposalStatus,
                hv.estado_tema AS topicStatus,
                hv.feedback_docente AS teacherFeedback,
                hv.fecha_envio AS submissionDate,
                hv.fecha_cambio AS changeDate,
                hv.motivo AS changeReason,
                pa.nombre AS academicPeriodName
            FROM historial_version_propuesta_estudiante hv
            LEFT JOIN opcion_titulacion ot ON ot.id_opcion = hv.id_opcion
            LEFT JOIN periodo_academico pa ON pa.id_periodo = hv.id_periodo
            WHERE hv.id_estudiante = :studentId
            ORDER BY hv.fecha_cambio DESC NULLS LAST, hv.id_historial DESC
        """;

        return queryList(sql,
                new MapSqlParameterSource("studentId", studentId),
                StudentProposalVersionHistoryItemDTO.class);
    }

    public Optional<StudentDirectorAssignmentReportDTO> findDirectorAssignmentByStudentId(Integer studentId) {
        String sql = """
            SELECT
                p.id_propuesta AS proposalId,
                ad.id AS assignmentId,
                ad.fecha_asignacion AS assignmentDate,
                ad.respuesta AS response,
                ad.observaciones AS observations,
                d.id_docente AS directorId,
                concat_ws(' ', u.nombres, u.apellidos) AS directorFullName,
                u.correo AS directorEmail,
                COALESCE(t.titulo, tp.titulo) AS topicTitle,
                p.estado AS proposalStatus
            FROM (
                SELECT *
                FROM propuesta_trabajo
                WHERE id_estudiante = :studentId
                ORDER BY fecha_envio DESC NULLS LAST, id_propuesta DESC
                LIMIT 1
            ) p
            LEFT JOIN LATERAL (
                SELECT *
                FROM asignacion_director x
                WHERE x.id_propuesta = p.id_propuesta
                ORDER BY x.id DESC
                LIMIT 1
            ) ad ON TRUE
            LEFT JOIN docente d ON d.id_docente = ad.id_docente
            LEFT JOIN usuario u ON u.id_usuario = d.id_usuario
            LEFT JOIN tema t ON t.id_tema = p.id_tema
            LEFT JOIN tema_propuesto_estudiante tp ON tp.id_tema_propuesto = p.id_tema_propuesto
        """;

        return queryOne(sql,
                new MapSqlParameterSource("studentId", studentId),
                StudentDirectorAssignmentReportDTO.class);
    }

    public Optional<StudentThesisProgressReportDTO> findThesisProgressByStudentId(Integer studentId) {
        String sql = """
            SELECT
                tt.id_trabajo AS thesisWorkId,
                tt.id_propuesta AS proposalId,
                tt.estado AS thesisStatus,
                pa.nombre AS academicPeriodName,
                COALESCE(t.titulo, tp.titulo) AS topicTitle,
                p.fecha_envio AS proposalSubmissionDate,
                p.estado AS proposalStatus,
                d.id_docente AS directorId,
                concat_ws(' ', u.nombres, u.apellidos) AS directorFullName,
                u.correo AS directorEmail
            FROM trabajo_titulacion tt
            LEFT JOIN periodo_academico pa ON pa.id_periodo = tt.id_periodo
            LEFT JOIN propuesta_trabajo p ON p.id_propuesta = tt.id_propuesta
            LEFT JOIN tema t ON t.id_tema = p.id_tema
            LEFT JOIN tema_propuesto_estudiante tp ON tp.id_tema_propuesto = p.id_tema_propuesto
            LEFT JOIN docente d ON d.id_docente = tt.id_director
            LEFT JOIN usuario u ON u.id_usuario = d.id_usuario
            WHERE tt.id_estudiante = :studentId
            ORDER BY tt.id_trabajo DESC
            LIMIT 1
        """;

        return queryOne(sql,
                new MapSqlParameterSource("studentId", studentId),
                StudentThesisProgressReportDTO.class);
    }

    public List<StudentTutoringItemDTO> findTutoringHistoryByStudentId(Integer studentId) {
        String sql = """
            SELECT
                t.id_tutoria AS tutoringId,
                t.id_trabajo AS thesisWorkId,
                t.fecha AS tutoringDate,
                t.tipo AS tutoringType,
                t.modalidad AS modality,
                t.lugar_enlace AS locationOrLink,
                t.asistencia AS attendance,
                t.registrada AS registered,
                t.observaciones AS observations,
                t.url_informe AS reportUrl,
                concat_ws(' ', u.nombres, u.apellidos) AS directorFullName,
                u.correo AS directorEmail
            FROM tutoria_trabajo t
            LEFT JOIN docente d ON d.id_docente = t.id_director
            LEFT JOIN usuario u ON u.id_usuario = d.id_usuario
            WHERE t.id_estudiante = :studentId
            ORDER BY t.fecha DESC NULLS LAST, t.id_tutoria DESC
        """;

        return queryList(sql,
                new MapSqlParameterSource("studentId", studentId),
                StudentTutoringItemDTO.class);
    }

    public Optional<StudentAdmissionRequestReportDTO> findLatestAdmissionRequestByEmail(String email) {
        String sql = """
            SELECT
                s.id_solicitud AS admissionRequestId,
                s.identificacion AS identification,
                s.nombres AS firstNames,
                s.apellidos AS lastNames,
                s.correo AS email,
                f.nombre AS facultyName,
                c.nombre AS careerName,
                pa.nombre AS academicPeriodName,
                s.fecha_envio AS submissionDate,
                s.estado AS status,
                s.observaciones AS observations,
                s.fecha_respuesta AS responseDate
            FROM solicitud_ingreso s
            LEFT JOIN facultad f ON f.id_facultad = s.id_facultad
            LEFT JOIN carrera c ON c.id_carrera = s.id_carrera
            LEFT JOIN periodo_academico pa ON pa.id_periodo = s.id_periodo
            WHERE LOWER(s.correo) = LOWER(:email)
            ORDER BY s.fecha_envio DESC NULLS LAST, s.id_solicitud DESC
            LIMIT 1
        """;

        return queryOne(sql,
                new MapSqlParameterSource("email", email),
                StudentAdmissionRequestReportDTO.class);
    }

    private <T> Optional<T> queryOne(String sql, MapSqlParameterSource params, Class<T> clazz) {
        List<T> results = jdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(clazz));
        return results.stream().findFirst();
    }

    private <T> List<T> queryList(String sql, MapSqlParameterSource params, Class<T> clazz) {
        List<T> results = jdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(clazz));
        return results == null ? Collections.emptyList() : results;
    }
}