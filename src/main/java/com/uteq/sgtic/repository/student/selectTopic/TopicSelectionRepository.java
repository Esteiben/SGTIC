package com.uteq.sgtic.repository.student.selectTopic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uteq.sgtic.entities.StudentDegreePeriod;

@Repository
public interface TopicSelectionRepository extends JpaRepository<StudentDegreePeriod, Integer> {

    @Query(value = "SELECT p_exito as exito, p_mensaje as mensaje FROM public.fn_en_seleccionar_o_cambiar_tema(" +
            ":idMatricula, :idOpcion, :idTema, :idTemaPropuesto, :motivo)", nativeQuery = true)
    TopicSelectionProjection executeTopicSelection(
            @Param("idMatricula") Integer idMatricula,
            @Param("idOpcion") Integer idOpcion,
            @Param("idTema") Integer idTema,
            @Param("idTemaPropuesto") Integer idTemaPropuesto,
            @Param("motivo") String motivo
    );  

    @Query(value = "SELECT p_exito as exito, p_mensaje as mensaje FROM public.fn_en_registrar_propuesta_estudiante(" +
            ":idMatricula, :idOpcion, :titulo, :descripcion, :urlDocumento)", nativeQuery = true)
    TopicSelectionProjection registrarPropuesta(
            @Param("idMatricula") Integer idMatricula,
            @Param("idOpcion") Integer idOpcion,
            @Param("titulo") String titulo,
            @Param("descripcion") String descripcion,
            @Param("urlDocumento") String urlDocumento
    );

    @Query(value = "SELECT p_exito as exito, p_mensaje as mensaje FROM public.fn_en_actualizar_propuesta_estudiante(" +
            ":idPropuesta, :idOpcion, :titulo, :descripcion, :urlDocumento)", nativeQuery = true)
    TopicSelectionProjection actualizarPropuesta(
            @Param("idPropuesta") Integer idPropuesta,
            @Param("idOpcion") Integer idOpcion,
            @Param("titulo") String titulo,
            @Param("descripcion") String descripcion,
            @Param("urlDocumento") String urlDocumento
    );

    public interface TopicSelectionStatusProjection {
        String getEstado_titulacion();
        String getTipo_tema_actual();
        Boolean getFuera_de_plazo();
        Boolean getDesactivado_por_plazo();
        Boolean getTiene_proceso_tema();
        Boolean getTiene_seleccion_banco();
        Boolean getPuede_seleccionar();
        Boolean getPuede_proponer();
        Boolean getPuede_cambiar_tema();
        Integer getCambios_tema_realizados();
        String getFecha_limite_seleccion();
        String getMensaje();
    }

    @Query(value = "SELECT * FROM public.fn_en_get_estado_seleccion_tema(:idEstudiante, :idPeriodo)", nativeQuery = true)
    TopicSelectionStatusProjection getEstadoSeleccion(@Param("idEstudiante") Integer idEstudiante, @Param("idPeriodo") Integer idPeriodo);
}