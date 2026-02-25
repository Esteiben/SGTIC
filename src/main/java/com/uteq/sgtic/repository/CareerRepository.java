package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerRepository extends JpaRepository<Career, Integer> {
    List<Career> findByActiveTrueOrderByNameAsc();
}