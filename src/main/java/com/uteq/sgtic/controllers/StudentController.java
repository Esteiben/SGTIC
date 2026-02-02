package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.StudentCreateDTO;
import com.uteq.sgtic.dtos.StudentResponseDTO;
import com.uteq.sgtic.entities.Career;
import com.uteq.sgtic.entities.Student;
import com.uteq.sgtic.entities.User;
import com.uteq.sgtic.repository.CareerRepository;
import com.uteq.sgtic.repository.StudentRepository;
import com.uteq.sgtic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final CareerRepository careerRepository;

    @GetMapping("/students")
    public ResponseEntity<List<StudentResponseDTO>> getAll() {
        List<StudentResponseDTO> list = studentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentResponseDTO> getById(@PathVariable Integer id) {
        return studentRepository.findById(id)
                .map(s -> ResponseEntity.ok(toResponse(s)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/students")
    public ResponseEntity<StudentResponseDTO> create(@RequestBody StudentCreateDTO dto) {
        if (dto.getIdUser() == null || dto.getIdCareer() == null) {
            return ResponseEntity.notFound().build();
        }

        java.util.Optional<User> userOpt = userRepository.findById(dto.getIdUser());
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        java.util.Optional<Career> careerOpt = careerRepository.findById(dto.getIdCareer());
        if (careerOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        Career career = careerOpt.get();

        Student student = new Student();
        student.setUser(user);
        student.setCareer(career);
        student.setStatus(dto.getStatus());
        student.setEntryDate(dto.getEntryDate());

        Student saved = studentRepository.save(student);
        return ResponseEntity.status(201).body(toResponse(saved));
    }

    private StudentResponseDTO toResponse(Student s) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setIdStudent(s.getIdStudent());
        dto.setIdUser(s.getUser() != null ? s.getUser().getIdUser() : null);
        dto.setIdCareer(s.getCareer() != null ? s.getCareer().getIdCareer() : null);
        dto.setStatus(s.getStatus());
        dto.setEntryDate(s.getEntryDate());
        return dto;
    }
}
