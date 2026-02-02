package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.DirectorAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionDirectorRepository extends JpaRepository<DirectorAssignment, Integer> {
}
