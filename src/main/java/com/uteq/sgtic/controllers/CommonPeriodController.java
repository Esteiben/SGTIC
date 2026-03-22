package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.AcademicPeriodDTO;
import com.uteq.sgtic.entities.AcademicPeriod;
import com.uteq.sgtic.repository.AcademicPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/common/periods") // <-- Fíjate que aquí dice "common", no "admin"
@RequiredArgsConstructor
public class CommonPeriodController {

    private final AcademicPeriodRepository academicPeriodRepository;

    @GetMapping("/active")
    public ResponseEntity<List<AcademicPeriodDTO>> getActivePeriods() {
        // Trae los periodos marcados como activos
        List<AcademicPeriod> periods = academicPeriodRepository.findByActiveTrueOrderByStartDateDesc();

        List<AcademicPeriodDTO> dtos = periods.stream()
                .map(p -> new AcademicPeriodDTO(
                        p.getIdPeriod(),
                        p.getName(),
                        p.getStartDate(),
                        p.getEndDate(),
                        p.getActive(),
                        p.getEnrollmentDeadline(),
                        p.getPlazoCambioTema(),
                        p.getMinimoAvances()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}