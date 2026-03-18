package com.uteq.sgtic.controllers.student;

import com.uteq.sgtic.dtos.student.EnrollmentRequestDTO;
import com.uteq.sgtic.dtos.student.EnrollmentResponseDTO;
import com.uteq.sgtic.services.student.IStudentDegreePeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/titulacion/matriculas")
@RequiredArgsConstructor
public class StudentDegreePeriodController {

    private final IStudentDegreePeriodService enrollmentService;

    @PostMapping("/matricular")
    public ResponseEntity<EnrollmentResponseDTO> enrollStudent(@RequestBody EnrollmentRequestDTO request) {
        EnrollmentResponseDTO response = enrollmentService.enrollStudent(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cerrar")
    public ResponseEntity<String> closePeriod(
            @PathVariable Integer id,
            @RequestParam boolean approved,
            @RequestParam(required = false) String observations) {
        
        enrollmentService.closePeriod(id, approved, observations);
        return ResponseEntity.ok("Periodo cerrado exitosamente con estado: " + (approved ? "APROBADO" : "REPROBADO"));
    }
}