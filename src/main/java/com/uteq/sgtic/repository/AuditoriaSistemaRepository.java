package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.AuditoriaSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaSistemaRepository extends JpaRepository<AuditoriaSistema, Integer> {

    List<AuditoriaSistema> findAllByOrderByFechaHoraDesc();
}