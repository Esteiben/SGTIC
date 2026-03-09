package com.uteq.sgtic.repository.student;

import com.uteq.sgtic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface StudentTopicSelectionRepository extends JpaRepository<User, Integer> {

    @Query(value = """
        SELECT e.id_estudiante
        FROM estudiante e
        WHERE e.id_usuario = :userId
        """, nativeQuery = true)
    Integer findStudentIdByUserId(@Param("userId") Integer userId);

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
        SELECT COUNT(1) > 0
        FROM estudiante_opcion_titulacion eot
        WHERE eot.id_estudiante = :idEstudiante
        """, nativeQuery = true)
    boolean existsStudentOptionSelection(@Param("idEstudiante") Integer idEstudiante);

    @Query(value = """
        SELECT COUNT(1) > 0
        FROM propuesta_trabajo pt
        WHERE pt.id_estudiante = :idEstudiante
          AND pt.estado IN ('pendiente', 'aprobada', 'correccion')
        """, nativeQuery = true)
    boolean existsActiveProposalByStudent(@Param("idEstudiante") Integer idEstudiante);

    @Query(value = """
        SELECT COUNT(1) > 0
        FROM trabajo_titulacion tt
        WHERE tt.id_estudiante = :idEstudiante
        """, nativeQuery = true)
    boolean existsWorkByStudent(@Param("idEstudiante") Integer idEstudiante);

    @Modifying
    @Query(value = """
        INSERT INTO estudiante_opcion_titulacion (
            id_estudiante,
            id_opcion,
            fecha_seleccion
        ) VALUES (
            :idEstudiante,
            :idOpcion,
            CURRENT_DATE
        )
        """, nativeQuery = true)
    int insertStudentOption(
            @Param("idEstudiante") Integer idEstudiante,
            @Param("idOpcion") Integer idOpcion
    );

    @Modifying
    @Query(value = """
        INSERT INTO propuesta_trabajo (
            id_estudiante,
            id_tema,
            id_periodo,
            fecha_envio,
            estado,
            observaciones
        ) VALUES (
            :idEstudiante,
            :idTema,
            :idPeriodo,
            CURRENT_DATE,
            'aprobada',
            'Selección realizada desde banco de temas'
        )
        """, nativeQuery = true)
    int insertSelectedTopicProposal(
            @Param("idEstudiante") Integer idEstudiante,
            @Param("idTema") Integer idTema,
            @Param("idPeriodo") Integer idPeriodo
    );
}