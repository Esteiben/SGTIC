package com.uteq.sgtic.repository.student.selectTopic;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uteq.sgtic.entities.DegreeOption;

@Repository
public interface ProcessSetupRepository extends JpaRepository<DegreeOption, Integer> {

        // ==============================================================================
        // 1. OBTENCIÓN DINÁMICA DEL ESTUDIANTE Y MATRÍCULA
        // ==============================================================================

        // NUEVO: Busca el ID del estudiante basado en el username o correo del token
        // JWT
        @Query(value = "SELECT e.id_estudiante FROM public.estudiante e " +
                        "JOIN public.usuario u ON e.id_usuario = u.id_usuario " +
                        "JOIN public.credencial c ON u.id_usuario = c.id_usuario " +
                        "WHERE c.username = :username OR u.correo = :username LIMIT 1", nativeQuery = true)
        Integer findIdEstudianteByUsername(@Param("username") String username);

        // Busca el ID de matrícula (id de la tabla estudiante_periodo_titulacion, vital
        // para guardar luego)
        @Query(value = "SELECT id FROM public.estudiante_periodo_titulacion WHERE id_estudiante = :idEstudiante AND id_periodo = :idPeriodo LIMIT 1", nativeQuery = true)
        Integer findIdMatricula(@Param("idEstudiante") Integer idEstudiante, @Param("idPeriodo") Integer idPeriodo);

        // ==============================================================================
        // 2. CONSULTAS A FUNCIONES DE POSTGRESQL
        // ==============================================================================

        // Llama a la función de opciones por estudiante
        @Query(value = "SELECT * FROM public.fn_en_get_opciones_estudiante(:idEstudiante, :idPeriodo)", nativeQuery = true)
        List<Map<String, Object>> getOptionsByStudentPeriod(
                        @Param("idEstudiante") Integer idEstudiante,
                        @Param("idPeriodo") Integer idPeriodo);

        // Llama a la función de temas por estudiante y opción
        @Query(value = "SELECT * FROM public.fn_en_get_temas_estudiante(:idEstudiante, :idPeriodo, :idOpcion)", nativeQuery = true)
        List<Map<String, Object>> getTemasDisponibles(
                        @Param("idEstudiante") Integer idEstudiante,
                        @Param("idPeriodo") Integer idPeriodo,
                        @Param("idOpcion") Integer idOpcion);

        // Llama a la función de historial de propuestas
        @Query(value = "SELECT * FROM public.fn_en_get_propuestas_estudiante(:idPeriodo, :idEstudiante)", nativeQuery = true)
        List<Map<String, Object>> getStudentProposals(
                        @Param("idPeriodo") Integer idPeriodo,
                        @Param("idEstudiante") Integer idEstudiante);

        @Query(value = "SELECT * FROM public.fn_en_get_historial_selecciones(:idEstudiante, :idPeriodo)", nativeQuery = true)
        List<TopicSelectionHistoryProjection> getTopicSelectionHistory(@Param("idEstudiante") Integer idEstudiante,
                        @Param("idPeriodo") Integer idPeriodo);

        @Query(value = "SELECT * FROM public.fn_en_get_historial_propuesta(:idPropuesta)", nativeQuery = true)
        List<StudentProposalHistoryProjection> getStudentProposalHistory(@Param("idPropuesta") Integer idPropuesta);

        interface TopicSelectionHistoryProjection {
                String getAccion();

                @JsonProperty("tituloTema")
                String getTitulo_tema();

                String getModalidad();

                String getFecha();
        }

        interface StudentProposalHistoryProjection {
                @JsonProperty("numeroVersion")
                Integer getNumero_version();

                @JsonProperty("esVersionActual")
                Boolean getEs_version_actual();

                String getTitulo();

                String getDescripcion();

                @JsonProperty("urlDocumento")
                String getUrl_documento();

                @JsonProperty("idOpcion")
                Integer getId_opcion();

                @JsonProperty("nombreOpcion")
                String getNombre_opcion();

                @JsonProperty("estadoPropuesta")
                String getEstado_propuesta();

                @JsonProperty("estadoTema")
                String getEstado_tema();

                @JsonProperty("feedbackDocente")
                String getFeedback_docente();

                @JsonProperty("fechaEnvio")
                String getFecha_envio();

                @JsonProperty("fechaMovimiento")
                String getFecha_movimiento();

                String getMotivo();
        }
}