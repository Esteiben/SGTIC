package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.Tribunal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TribunalRepository extends JpaRepository<Tribunal, Integer> {
}
