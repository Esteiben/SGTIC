package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.ProposalApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AprobacionComisionRepository extends JpaRepository<ProposalApproval, Integer> {
}
