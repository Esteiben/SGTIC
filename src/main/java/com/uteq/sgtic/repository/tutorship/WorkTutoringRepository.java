package com.uteq.sgtic.repository.tutorship;

import com.uteq.sgtic.entities.WorkTutoring;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkTutoringRepository extends JpaRepository<WorkTutoring, Integer> {
    List<WorkTutoring> findByDirector_IdTeacherOrderByDateDesc(Integer idTeacher);
}
