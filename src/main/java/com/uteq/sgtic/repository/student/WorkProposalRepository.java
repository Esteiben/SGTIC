package com.uteq.sgtic.repository.student;

import com.uteq.sgtic.entities.WorkProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkProposalRepository extends JpaRepository<WorkProposal, Integer> {
    // Busca la propuesta aprobada o en proceso de un estudiante en un periodo específico (usando la matrícula)
    Optional<WorkProposal> findFirstByStudentIdStudentAndAcademicPeriodIdPeriodOrderByIdProposalDesc(Integer idStudent, Integer idPeriod);
}