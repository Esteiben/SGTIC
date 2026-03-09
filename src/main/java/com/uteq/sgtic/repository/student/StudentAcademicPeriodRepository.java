package com.uteq.sgtic.repository.student;

import com.uteq.sgtic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentAcademicPeriodRepository extends JpaRepository<User, Integer> {

    interface AcceptedAcademicPeriodProjection {
        Integer getIdPeriod();
        String getName();
        LocalDate getStartDate();
        LocalDate getEndDate();
        Boolean getActive();
        LocalDate getEnrollmentDeadline();
    }

    @Query(value = """
        SELECT DISTINCT
            p.id_periodo AS idPeriod,
            p.nombre AS name,
            CAST(p.fecha_inicio AS DATE) AS startDate,
            CAST(p.fecha_fin AS DATE) AS endDate,
            p.activo AS active,
            CAST(p.fecha_limite_matriculacion AS DATE) AS enrollmentDeadline
        FROM solicitud_ingreso si
        JOIN usuario u ON u.identificacion = si.identificacion
        JOIN periodo_academico p ON p.id_periodo = si.id_periodo
        WHERE u.id_usuario = :userId
          AND si.estado = 'aprobada'
        ORDER BY CAST(p.fecha_inicio AS DATE) DESC
        """, nativeQuery = true)
    List<AcceptedAcademicPeriodProjection> findAcceptedPeriodsByUserId(@Param("userId") Integer userId);
}