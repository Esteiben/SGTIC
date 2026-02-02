package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.CouncilMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsejoMiembroRepository extends JpaRepository<CouncilMember, Integer> {
}
