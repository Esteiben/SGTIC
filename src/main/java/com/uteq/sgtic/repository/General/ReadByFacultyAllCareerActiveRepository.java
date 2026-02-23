package com.uteq.sgtic.repository.General;

import com.uteq.sgtic.entities.Career;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadByFacultyAllCareerActiveRepository extends JpaRepository<Career, Integer> {
    List<Career> findByFaculty_IdFacultyAndActiveTrue(Integer idFaculty);
}
