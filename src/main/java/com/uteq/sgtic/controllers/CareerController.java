package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.CareerCreateDTO;
import com.uteq.sgtic.dtos.CareerResponseDTO;
import com.uteq.sgtic.entities.Career;
import com.uteq.sgtic.entities.Faculty;
import com.uteq.sgtic.repository.CareerRepository;
import com.uteq.sgtic.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CareerController {

    private final CareerRepository careerRepository;
    private final FacultyRepository facultyRepository;

    @GetMapping("/careers")
    public ResponseEntity<List<CareerResponseDTO>> getAll() {
        List<CareerResponseDTO> list = careerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/careers/{id}")
    public ResponseEntity<CareerResponseDTO> getById(@PathVariable Integer id) {
        return careerRepository.findById(id)
                .map(c -> ResponseEntity.ok(toResponse(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/careers")
    public ResponseEntity<CareerResponseDTO> create(@RequestBody CareerCreateDTO dto) {
        // If the FK id is missing or the referenced Faculty does not exist, return 404
        if (dto.getIdFaculty() == null) {
            return ResponseEntity.notFound().build();
        }

        return facultyRepository.findById(dto.getIdFaculty())
                .map(faculty -> {
                    Career career = new Career();
                    career.setFaculty(faculty);
                    career.setName(dto.getName());
                    career.setActive(dto.getActive());

                    Career saved = careerRepository.save(career);
                    return ResponseEntity.status(201).body(toResponse(saved));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private CareerResponseDTO toResponse(Career c) {
        CareerResponseDTO dto = new CareerResponseDTO();
        dto.setIdCareer(c.getIdCareer());
        dto.setIdFaculty(c.getFaculty() != null ? c.getFaculty().getIdFaculty() : null);
        dto.setName(c.getName());
        dto.setActive(c.getActive());
        return dto;
    }
}
