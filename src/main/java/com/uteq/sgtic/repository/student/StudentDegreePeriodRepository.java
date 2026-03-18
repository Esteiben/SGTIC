package com.uteq.sgtic.repository.student;

import com.uteq.sgtic.entities.StudentDegreePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentDegreePeriodRepository extends JpaRepository<StudentDegreePeriod, Integer> {
    
    // Buscar si el estudiante ya tiene una matrícula en curso en CUALQUIER periodo
    boolean existsByStudentIdStudentAndPeriodStatus(Integer idStudent, String periodStatus);

    // Obtener la última matrícula finalizada del estudiante para determinar el nivel
    @Query("SELECT s FROM StudentDegreePeriod s WHERE s.student.idStudent = :idStudent AND s.periodStatus IN ('APROBADO', 'REPROBADO') ORDER BY s.closingDate DESC LIMIT 1")
    Optional<StudentDegreePeriod> findLastFinishedPeriodByStudent(Integer idStudent);

    // Obtener todas las matrículas de un estudiante (Historial)
    List<StudentDegreePeriod> findByStudentIdStudentOrderByEnrollmentDateDesc(Integer idStudent);

    // ---> ESTA ES LA LÍNEA QUE TE FALTABA <---
    // Buscar la matrícula de un estudiante en un periodo específico
    Optional<StudentDegreePeriod> findByStudentIdStudentAndAcademicPeriodIdPeriod(Integer idStudent, Integer idPeriod);
}