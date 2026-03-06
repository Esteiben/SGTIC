package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.AcademicPeriodDTO;
import com.uteq.sgtic.dtos.CareerDTO;
import com.uteq.sgtic.entities.AcademicPeriod;
import com.uteq.sgtic.entities.Career;
import com.uteq.sgtic.repository.AcademicPeriodRepository;
import com.uteq.sgtic.repository.CareerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/catalog")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // ← Agrega esto para CORS
public class CatalogController {

    private final CareerRepository careerRepository;
    private final AcademicPeriodRepository academicPeriodRepository;
    // ============ CARRERAS ============
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
    @GetMapping("/periods")
    public ResponseEntity<List<AcademicPeriodDTO>> getAllPeriods() {
        List<AcademicPeriod> periods = academicPeriodRepository.findAllByOrderByStartDateDesc();

        List<AcademicPeriodDTO> dtos = periods.stream()
                .map(p -> new AcademicPeriodDTO(
                        p.getIdPeriod(),
                        p.getName(),
                        p.getStartDate(),
                        p.getEndDate(),
                        p.getActive()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // Obtener solo períodos activos
    @GetMapping("/periods/active")
    public ResponseEntity<List<AcademicPeriodDTO>> getActivePeriods() {
        List<AcademicPeriod> periods = academicPeriodRepository.findByActiveTrueOrderByStartDateDesc();

        List<AcademicPeriodDTO> dtos = periods.stream()
                .map(p -> new AcademicPeriodDTO(
                        p.getIdPeriod(),
                        p.getName(),
                        p.getStartDate(),
                        p.getEndDate(),
                        p.getActive()  // ← AGREGADO el campo active
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
    @PostMapping("/periods")
    public ResponseEntity<AcademicPeriodDTO> createPeriod(@RequestBody AcademicPeriodDTO periodDTO) {
        AcademicPeriod period = new AcademicPeriod();
        period.setName(periodDTO.getName());
        period.setStartDate(periodDTO.getStartDate());
        period.setEndDate(periodDTO.getEndDate());
        period.setActive(true);
        if (periodDTO.getStartDate() != null) {
            period.setEnrollmentDeadline(periodDTO.getStartDate());
        } else {
            period.setEnrollmentDeadline(LocalDate.now());
        }
        AcademicPeriod savedPeriod = academicPeriodRepository.save(period);

        return ResponseEntity.ok(new AcademicPeriodDTO(
                savedPeriod.getIdPeriod(),
                savedPeriod.getName(),
                savedPeriod.getStartDate(),
                savedPeriod.getEndDate(),
                savedPeriod.getActive()
        ));
    }
    // Actualizar un período
    @PutMapping("/periods/{id}")
    public ResponseEntity<AcademicPeriodDTO> updatePeriod(
            @PathVariable Integer id,
            @RequestBody AcademicPeriodDTO periodDTO) {

        AcademicPeriod period = academicPeriodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Período no encontrado"));

         period.setName(periodDTO.getName());
         period.setStartDate(periodDTO.getStartDate());
          period.setEndDate(periodDTO.getEndDate());
          period.setActive(periodDTO.getActive());
        // Asignar fecha límite si viene null
        if (period.getEnrollmentDeadline() == null) {
            period.setEnrollmentDeadline(periodDTO.getStartDate() != null ?
                    periodDTO.getStartDate() : LocalDate.now());
        }

         AcademicPeriod updatedPeriod = academicPeriodRepository.save(period);

            return ResponseEntity.ok(new AcademicPeriodDTO(
                    updatedPeriod.getIdPeriod(),
                updatedPeriod.getName(),
                updatedPeriod.getStartDate(),
                updatedPeriod.getEndDate(),
                updatedPeriod.getActive()
            )   );
    }
    @DeleteMapping("/periods/{id}")
    public ResponseEntity<Void> deletePeriod(@PathVariable Integer id) {
        academicPeriodRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}