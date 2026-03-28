package com.uteq.sgtic.repository.InstitutionalStructurePackRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uteq.sgtic.entities.Faculty;

@Repository
public interface FacultyDashboardRepository extends JpaRepository <Faculty, Integer> {}
