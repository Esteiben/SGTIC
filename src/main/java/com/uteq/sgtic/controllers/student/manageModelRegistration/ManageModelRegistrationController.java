package com.uteq.sgtic.controllers.student.manageModelRegistration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.uteq.sgtic.dtos.student.manageModelRegistration.EnrollmentResponseDTO;
import com.uteq.sgtic.services.student.manageModelRegistration.IManageModelRegistrationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student/registration")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ManageModelRegistrationController {

    private final IManageModelRegistrationService registrationService;

    @PostMapping("/auto-enroll/{studentId}")
    public ResponseEntity<EnrollmentResponseDTO> autoEnroll(@PathVariable Integer studentId) {
        EnrollmentResponseDTO response = registrationService.autoEnrollStudent(studentId);
        return ResponseEntity.ok(response);
    }
}