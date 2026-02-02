package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.WorkProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropuestaTrabajoRepository extends JpaRepository<WorkProposal, Integer> {
}
