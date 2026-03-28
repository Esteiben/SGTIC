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
@RequestMapping("/api/admin/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final AcademicPeriodRepository academicPeriodRepository;

    @GetMapping("/periods/active")
    public ResponseEntity<List<AcademicPeriodDTO>> getActivePeriods() {
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