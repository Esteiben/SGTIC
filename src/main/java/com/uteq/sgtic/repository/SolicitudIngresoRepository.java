package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.AdmissionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudIngresoRepository extends JpaRepository<AdmissionRequest, Integer> {
}
