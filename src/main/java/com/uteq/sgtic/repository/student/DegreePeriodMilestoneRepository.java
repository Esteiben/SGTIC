package com.uteq.sgtic.repository.student;

import com.uteq.sgtic.entities.DegreePeriodMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DegreePeriodMilestoneRepository extends JpaRepository<DegreePeriodMilestone, Integer> {
    
    // Busca el estado de un hito específico (ej. "ANTEPROYECTO", "PREDEFENSA", "DEFENSA_FINAL") asociado a la matrícula actual
    Optional<DegreePeriodMilestone> findByStudentDegreePeriodIdAndMilestoneType(Integer idMatricula, String milestoneType);
    
}