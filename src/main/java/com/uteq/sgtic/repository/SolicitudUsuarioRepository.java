package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.AdmissionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudUsuarioRepository extends JpaRepository<AdmissionUser, Integer> {
}
