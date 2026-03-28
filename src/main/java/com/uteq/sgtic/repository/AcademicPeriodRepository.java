package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.AcademicPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicPeriodRepository extends JpaRepository<AcademicPeriod, Integer> {
    List<AcademicPeriod> findByActiveTrueOrderByStartDateDesc();

}