package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.CommitteeMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComisionMiembroRepository extends JpaRepository<CommitteeMember, Integer> {
}
