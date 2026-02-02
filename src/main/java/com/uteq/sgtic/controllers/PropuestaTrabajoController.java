package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.WorkProposalDto;
import com.uteq.sgtic.entities.WorkProposal;
import com.uteq.sgtic.entities.Student;
import com.uteq.sgtic.repository.PropuestaTrabajoRepository;
import com.uteq.sgtic.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PropuestaTrabajoController {

    private final PropuestaTrabajoRepository propuestaRepository;
    private final StudentRepository studentRepository;

    @GetMapping("/propuestas")
    public ResponseEntity<List<WorkProposalDto>> getAll() {
        List<WorkProposalDto> list = propuestaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/propuestas/{id}")
    public ResponseEntity<WorkProposalDto> getById(@PathVariable Integer id) {
        return propuestaRepository.findById(id)
                .map(p -> ResponseEntity.ok(toResponse(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/propuestas")
    public ResponseEntity<WorkProposalDto> create(@RequestBody WorkProposalDto dto) {
        if (dto.getIdStudent() == null) {
            return ResponseEntity.notFound().build();
        }

        java.util.Optional<Student> studentOpt = studentRepository.findById(dto.getIdStudent());
        if (studentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = studentOpt.get();

        WorkProposal p = new WorkProposal();
        p.setStudent(student);
        p.setTitle(dto.getTitle());
        p.setDescription(dto.getDescription());
        p.setRegistrationDate(dto.getRegistrationDate());
        p.setStatus(dto.getStatus());

        WorkProposal saved = propuestaRepository.save(p);
        return ResponseEntity.status(201).body(toResponse(saved));
    }

    private WorkProposalDto toResponse(WorkProposal p) {
        WorkProposalDto dto = new WorkProposalDto();
        dto.setIdProposal(p.getIdProposal());
        dto.setIdStudent(p.getStudent() != null ? p.getStudent().getIdStudent() : null);
        dto.setTitle(p.getTitle());
        dto.setDescription(p.getDescription());
        dto.setRegistrationDate(p.getRegistrationDate());
        dto.setStatus(p.getStatus());
        return dto;
    }
}
