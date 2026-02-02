package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.TribunalMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TribunalMiembroRepository extends JpaRepository<TribunalMember, Integer> {
}
