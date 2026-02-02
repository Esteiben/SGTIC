package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.TeacherCareer;
import com.uteq.sgtic.entities.TeacherCareerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherCareerRepository extends JpaRepository<TeacherCareer, TeacherCareerId> {
}
