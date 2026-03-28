package com.uteq.sgtic.repository.tutorship;

import com.uteq.sgtic.entities.WorkTutoring;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkTutoringRepository extends JpaRepository<WorkTutoring, Integer> {
    List<WorkTutoring> findByDegreeWork_Director_IdTeacherOrderByDateDesc(Integer idTeacher);
    long countByDegreeWork_Student_IdStudentAndDegreeWork_AcademicPeriod_IdPeriodAndRegisteredTrue(Integer idStudent, Integer idPeriod);
}
