package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.AcademicPeriodDTO;
import com.uteq.sgtic.dtos.CareerDTO;
import com.uteq.sgtic.entities.AcademicPeriod;
import com.uteq.sgtic.entities.Career;
import com.uteq.sgtic.repository.AcademicPeriodRepository;
import com.uteq.sgtic.repository.CareerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CareerRepository careerRepository;
    private final AcademicPeriodRepository academicPeriodRepository;

    @GetMapping("/careers/active")
    public ResponseEntity<List<CareerDTO>> getActiveCareers() {
        List<Career> careers = careerRepository.findByActiveTrueOrderByNameAsc();

        List<CareerDTO> dtos = careers.stream()
                .map(c -> new CareerDTO(
                        c.getIdCareer(),
                        c.getName(),
                        c.getFaculty() != null ? c.getFaculty().getName() : null
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/periods/active")
    public ResponseEntity<List<AcademicPeriodDTO>> getActivePeriods() {
        List<AcademicPeriod> periods = academicPeriodRepository.findByActiveTrueOrderByStartDateDesc();

        List<AcademicPeriodDTO> dtos = periods.stream()
                .map(p -> new AcademicPeriodDTO(
                        p.getIdPeriod(),
                        p.getName(),
                        p.getStartDate(),
                        p.getEndDate()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}