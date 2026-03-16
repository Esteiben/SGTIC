package com.uteq.sgtic.repository.tutorship;

import com.uteq.sgtic.entities.DegreeWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DegreeWorkRepository extends JpaRepository<DegreeWork, Integer> {
    List<DegreeWork> findByDirector_IdTeacher(Integer idTeacher);
}
