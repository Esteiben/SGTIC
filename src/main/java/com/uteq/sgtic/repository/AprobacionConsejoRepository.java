package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.CouncilApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AprobacionConsejoRepository extends JpaRepository<CouncilApproval, Integer> {
}
