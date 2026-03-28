package com.uteq.sgtic.repository.student.manageModelRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uteq.sgtic.entities.DegreePeriodDelivery;

@Repository
public interface ManageModelRegistrationRepository extends JpaRepository<DegreePeriodDelivery, Integer> {

    @Query(value = "SELECT * FROM fn_en_procesar_matricula_estudiante(:idEstudiante)", nativeQuery = true)
    EnrollmentProjection processAutoEnrollment(@Param("idEstudiante") Integer idEstudiante);

    interface EnrollmentProjection {
        Boolean getExito();
        Integer getId_matricula();
        Integer getNivel_matriculado();
        String getMensaje();
    }
}