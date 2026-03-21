package com.uteq.sgtic.repository.student.selectTopic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uteq.sgtic.entities.StudentDegreePeriod;

@Repository
public interface TopicSelectionRepository extends JpaRepository<StudentDegreePeriod, Integer> {

    // 1. MÉTODO PARA SELECCIONAR O CAMBIAR TEMA DEL BANCO
    @Query(value = "SELECT p_exito as exito, p_mensaje as mensaje FROM public.fn_en_seleccionar_o_cambiar_tema(" +
            ":idMatricula, :idOpcion, :idTema, :idTemaPropuesto, :motivo)", nativeQuery = true)
    TopicSelectionProjection executeTopicSelection(
            @Param("idMatricula") Integer idMatricula,
            @Param("idOpcion") Integer idOpcion,
            @Param("idTema") Integer idTema,
            @Param("idTemaPropuesto") Integer idTemaPropuesto,
            @Param("motivo") String motivo
    );

    // 2. MÉTODO PARA REGISTRAR UNA NUEVA PROPUESTA
    @Query(value = "SELECT p_exito as exito, p_mensaje as mensaje FROM public.fn_en_registrar_propuesta_estudiante(" +
            ":idMatricula, :idOpcion, :titulo, :descripcion, :urlDocumento)", nativeQuery = true)
    TopicSelectionProjection registrarPropuesta(
            @Param("idMatricula") Integer idMatricula,
            @Param("idOpcion") Integer idOpcion,
            @Param("titulo") String titulo,
            @Param("descripcion") String descripcion,
            @Param("urlDocumento") String urlDocumento
    );
}