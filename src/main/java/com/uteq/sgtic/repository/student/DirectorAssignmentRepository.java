package com.uteq.sgtic.repository.student;

import com.uteq.sgtic.entities.DirectorAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DirectorAssignmentRepository extends JpaRepository<DirectorAssignment, Integer> {
    
    // Busca si una propuesta de trabajo específica ya tiene un director asignado y evalúa su respuesta (ej. "aceptado")
    Optional<DirectorAssignment> findByWorkProposalIdProposalAndResponse(Integer idProposal, String response);
    
}