package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.FacultyCouncil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsejoFacultadRepository extends JpaRepository<FacultyCouncil, Integer> {
}
