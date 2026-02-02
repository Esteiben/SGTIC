package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.ResearchCommittee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComisionInvestigacionRepository extends JpaRepository<ResearchCommittee, Integer> {
}
