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
@RequestMapping("/api/admin/catalog/periods")
@RequiredArgsConstructor
public class AcademicPeriodController {

    private final AcademicPeriodRepository academicPeriodRepository;

    // GET todos los períodos
    @GetMapping
    public ResponseEntity<List<AcademicPeriodDTO>> getAllPeriods() {
        List<AcademicPeriod> periods = academicPeriodRepository.findAll();
        List<AcademicPeriodDTO> dtos = periods.stream()
                .map(p -> new AcademicPeriodDTO(
                        p.getIdPeriod(),
                        p.getName(),
                        p.getStartDate(),
                        p.getEndDate(),
                        p.getActive(),
                        p.getEnrollmentDeadline()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // POST crear período
    @PostMapping
    public ResponseEntity<AcademicPeriod> createPeriod(@RequestBody AcademicPeriod period) {
        period.setIdPeriod(null); // Asegurar que genere nuevo ID
        AcademicPeriod saved = academicPeriodRepository.save(period);
        return ResponseEntity.ok(saved);
    }

    // PUT actualizar período
    @PutMapping("/{id}")
    public ResponseEntity<AcademicPeriod> updatePeriod(
            @PathVariable Integer id,
            @RequestBody AcademicPeriod period) {

        return academicPeriodRepository.findById(id)
                .map(existing -> {
                    existing.setName(period.getName());
                    existing.setStartDate(period.getStartDate());
                    existing.setEndDate(period.getEndDate());
                    existing.setActive(period.getActive());
                    if (period.getEnrollmentDeadline() != null) {
                        existing.setEnrollmentDeadline(period.getEnrollmentDeadline());
                    }
                    return ResponseEntity.ok(academicPeriodRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE eliminar período
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePeriod(@PathVariable Integer id) {
        if (!academicPeriodRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        academicPeriodRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}