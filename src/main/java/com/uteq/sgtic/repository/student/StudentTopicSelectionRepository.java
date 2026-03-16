package com.uteq.sgtic.repository.student;

import com.uteq.sgtic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudentTopicSelectionRepository extends JpaRepository<User, Integer> {

    // ===================================================================================
    // PROJECTIONS
    // ===================================================================================

    interface CurrentProposalProjection {
        Integer getIdPropuesta();
        Integer getIdTema();
        Integer getIdTemaPropuesto();
        Integer getIdPeriodo();
        Integer getIdOpcion();
        String getEstado();
        LocalDate getFechaEnvio();
    }

    interface StudentProposalSummaryProjection {
        Integer getIdPropuesta();
        Integer getIdTemaPropuesto();
        String getTitulo();
        String getDescripcion();
        String getEstadoPropuesta();
        String getEstadoTema();
        String getFeedbackDocente();
        String getUrlDocumento();
        Integer getIdOpcion();
        String getNombreOpcion();
        Integer getNumeroVersion();
        Integer getTotalVersiones();
        LocalDate getFechaEnvio();
        LocalDateTime getFechaUltimaActualizacion();
        Boolean getEditable();
    }

    interface StudentProposalHistoryProjection {
        Integer getNumeroVersion();
        Boolean getEsVersionActual();
        String getTitulo();
        String getDescripcion();
        String getUrlDocumento();
        Integer getIdOpcion();
        String getNombreOpcion();
        String getEstadoPropuesta();
        String getEstadoTema();
        String getFeedbackDocente();
        LocalDate getFechaEnvio();
        LocalDateTime getFechaMovimiento();
        String getMotivo();
    }

    // ===================================================================================
    // QUERIES
    // ===================================================================================

    @Query(value = """
        SELECT e.id_estudiante
        FROM estudiante e
        WHERE e.id_usuario = :userId
        """, nativeQuery = true)
    Integer findStudentIdByUserId(@Param("userId") Integer userId);

    @Query(value = """
        SELECT e.estado_titulacion
        FROM estudiante e
        WHERE e.id_estudiante = :idEstudiante
        """, nativeQuery = true)
    String findStudentStatus(@Param("idEstudiante") Integer idEstudiante);

    @Query(value = """
        SELECT COUNT(1) > 0
        FROM periodo_academico p
        WHERE p.id_periodo = :idPeriodo
          AND p.activo = true
        """, nativeQuery = true)
    boolean existsActivePeriod(@Param("idPeriodo") Integer idPeriodo);

    @Query(value = """
        SELECT p.fecha_inicio
        FROM periodo_academico p
        WHERE p.id_periodo = :idPeriodo
        """, nativeQuery = true)
    LocalDate findPeriodStartDate(@Param("idPeriodo") Integer idPeriodo);

    @Query(value = """
        SELECT public.fn_fecha_limite_seleccion(:idPeriodo)
        """, nativeQuery = true)
    LocalDate findSelectionDeadline(@Param("idPeriodo") Integer idPeriodo);

    @Query(value = """
        SELECT COUNT(1) > 0
        FROM vista_temas_disponibles v
        WHERE v.id_tema = :idTema
          AND v.id_carrera = :idCarrera
          AND v.id_opcion = :idOpcion
        """, nativeQuery = true)
    boolean existsAvailableTopic(
            @Param("idTema") Integer idTema,
            @Param("idCarrera") Integer idCarrera,
            @Param("idOpcion") Integer idOpcion
    );

    @Query(value = """
        SELECT
            pt.id_propuesta AS idPropuesta,
            pt.id_tema AS idTema,
            pt.id_tema_propuesto AS idTemaPropuesto,
            pt.id_periodo AS idPeriodo,
            COALESCE(t.id_opcion, tpe.id_opcion) AS idOpcion,
            pt.estado AS estado,
            pt.fecha_envio AS fechaEnvio
        FROM propuesta_trabajo pt
        LEFT JOIN tema t
               ON t.id_tema = pt.id_tema
        LEFT JOIN tema_propuesto_estudiante tpe
               ON tpe.id_tema_propuesto = pt.id_tema_propuesto
        WHERE pt.id_estudiante = :idEstudiante
          AND pt.id_periodo = :idPeriodo
          AND pt.estado IN ('pendiente', 'aprobada', 'correccion')
        ORDER BY pt.fecha_envio DESC, pt.id_propuesta DESC
        LIMIT 1
        """, nativeQuery = true)
    CurrentProposalProjection findLatestActiveProposalByStudentAndPeriod(
            @Param("idEstudiante") Integer idEstudiante,
            @Param("idPeriodo") Integer idPeriodo
    );

    @Query(value = """
        SELECT COUNT(1)
        FROM historial_cambio_tema hct
        WHERE hct.id_estudiante = :idEstudiante
          AND hct.id_periodo = :idPeriodo
        """, nativeQuery = true)
    Integer countTopicChangesByStudentAndPeriod(
            @Param("idEstudiante") Integer idEstudiante,
            @Param("idPeriodo") Integer idPeriodo
    );

    @Query(value = """
        SELECT COUNT(1) > 0
        FROM trabajo_titulacion tt
        WHERE tt.id_estudiante = :idEstudiante
          AND tt.id_periodo = :idPeriodo
        """, nativeQuery = true)
    boolean existsWorkByStudentAndPeriod(
            @Param("idEstudiante") Integer idEstudiante,
            @Param("idPeriodo") Integer idPeriodo
    );

    @Query(value = """
        SELECT public.fn_guardar_seleccion_tema_banco(
            :userId,
            :idCarrera,
            :idPeriodo,
            :idTema,
            :idOpcion
        )
        """, nativeQuery = true)
    Integer saveBankTopicSelection(
            @Param("userId") Integer userId,
            @Param("idCarrera") Integer idCarrera,
            @Param("idPeriodo") Integer idPeriodo,
            @Param("idTema") Integer idTema,
            @Param("idOpcion") Integer idOpcion
    );

    @Query(value = """
        SELECT public.fn_registrar_propuesta_tema_estudiante(
            :userId,
            :idPeriodo,
            :idOpcion,
            :titulo,
            :descripcion,
            :urlDocumento
        )
        """, nativeQuery = true)
    Integer registerStudentTopicProposal(
            @Param("userId") Integer userId,
            @Param("idPeriodo") Integer idPeriodo,
            @Param("idOpcion") Integer idOpcion,
            @Param("titulo") String titulo,
            @Param("descripcion") String descripcion,
            @Param("urlDocumento") String urlDocumento
    );

    // ===================================================================================
    // NUEVOS MÉTODOS AGREGADOS
    // ===================================================================================

    @Query(value = """
        SELECT
            v.id_propuesta AS idPropuesta,
            v.id_tema_propuesto AS idTemaPropuesto,
            v.titulo AS titulo,
            v.descripcion AS descripcion,
            v.estado_propuesta AS estadoPropuesta,
            v.estado_tema AS estadoTema,
            v.feedback_docente AS feedbackDocente,
            v.url_documento AS urlDocumento,
            v.id_opcion AS idOpcion,
            v.nombre_opcion AS nombreOpcion,
            v.numero_version AS numeroVersion,
            v.total_versiones AS totalVersiones,
            v.fecha_envio AS fechaEnvio,
            v.fecha_ultima_actualizacion AS fechaUltimaActualizacion,
            v.editable AS editable
        FROM public.vw_propuestas_estudiante_resumen v
        WHERE v.id_estudiante = :idEstudiante
          AND v.id_periodo = :idPeriodo
        ORDER BY v.fecha_envio DESC, v.id_propuesta DESC
        """, nativeQuery = true)
    List<StudentProposalSummaryProjection> findStudentProposalSummaries(
            @Param("idEstudiante") Integer idEstudiante,
            @Param("idPeriodo") Integer idPeriodo
    );

    @Query(value = """
        SELECT
            v.numero_version AS numeroVersion,
            v.es_version_actual AS esVersionActual,
            v.titulo AS titulo,
            v.descripcion AS descripcion,
            v.url_documento AS urlDocumento,
            v.id_opcion AS idOpcion,
            v.nombre_opcion AS nombreOpcion,
            v.estado_propuesta AS estadoPropuesta,
            v.estado_tema AS estadoTema,
            v.feedback_docente AS feedbackDocente,
            v.fecha_envio AS fechaEnvio,
            v.fecha_movimiento AS fechaMovimiento,
            v.motivo AS motivo
        FROM public.vw_historial_propuesta_estudiante v
        WHERE v.id_estudiante = :idEstudiante
          AND v.id_propuesta = :idPropuesta
        ORDER BY v.numero_version DESC, v.es_version_actual DESC
        """, nativeQuery = true)
    List<StudentProposalHistoryProjection> findStudentProposalHistory(
            @Param("idEstudiante") Integer idEstudiante,
            @Param("idPropuesta") Integer idPropuesta
    );

    @Query(value = """
        SELECT public.fn_actualizar_propuesta_tema_estudiante(
            :userId,
            :idPropuesta,
            :idOpcion,
            :titulo,
            :descripcion,
            :urlDocumento
        )
        """, nativeQuery = true)
    Integer updateStudentTopicProposal(
            @Param("userId") Integer userId,
            @Param("idPropuesta") Integer idPropuesta,
            @Param("idOpcion") Integer idOpcion,
            @Param("titulo") String titulo,
            @Param("descripcion") String descripcion,
            @Param("urlDocumento") String urlDocumento
    );

}