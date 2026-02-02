package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.FacultyCreateDTO;
import com.uteq.sgtic.dtos.FacultyResponseDTO;
import com.uteq.sgtic.entities.Faculty;
import com.uteq.sgtic.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyRepository facultyRepository;

    @GetMapping("/faculties")
    public ResponseEntity<List<FacultyResponseDTO>> getAll() {
        List<FacultyResponseDTO> list = facultyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/faculties/{id}")
    public ResponseEntity<FacultyResponseDTO> getById(@PathVariable Integer id) {
        return facultyRepository.findById(id)
                .map(f -> ResponseEntity.ok(toResponse(f)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/faculties")
    public ResponseEntity<FacultyResponseDTO> create(@RequestBody FacultyCreateDTO dto) {
        Faculty entity = new Faculty();
        entity.setName(dto.getName());
        entity.setActive(dto.getActive());

        Faculty saved = facultyRepository.save(entity);
        return ResponseEntity.status(201).body(toResponse(saved));
    }

    private FacultyResponseDTO toResponse(Faculty f) {
        FacultyResponseDTO dto = new FacultyResponseDTO();
        dto.setIdFaculty(f.getIdFaculty());
        dto.setName(f.getName());
        dto.setActive(f.getActive());
        return dto;
    }
}
