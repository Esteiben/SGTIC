package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.DegreeWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrabajoTitulacionRepository extends JpaRepository<DegreeWork, Integer> {
}
