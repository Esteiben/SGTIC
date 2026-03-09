package com.uteq.sgtic.repository.requestAccess;

import com.uteq.sgtic.entities.AdmissionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestAccessRepository extends JpaRepository<AdmissionRequest, Integer> {

    boolean existsByIdentificationAndAcademicPeriod_IdPeriodAndStatusIn(
            String identification,
            Integer idPeriod,
            List<String> statuses
    );

    boolean existsByEmailAndAcademicPeriod_IdPeriodAndStatusIn(
            String email,
            Integer idPeriod,
            List<String> statuses
    );
}