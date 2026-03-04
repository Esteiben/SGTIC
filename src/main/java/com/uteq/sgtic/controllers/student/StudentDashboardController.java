package com.uteq.sgtic.controllers.student;

import com.uteq.sgtic.dtos.student.DashboardStatusDTO;
import com.uteq.sgtic.services.student.IStudentDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/student/dashboard")
@RequiredArgsConstructor
public class StudentDashboardController {

    private final IStudentDashboardService dashboardService;
    // Asumo que tienes el UserRepository para obtener el ID por email como antes
    private final com.uteq.sgtic.repository.UserRepository userRepository; 

    @GetMapping("/status")
    public ResponseEntity<DashboardStatusDTO> getStatus(Principal principal) {
        String email = principal.getName();
        
        Integer userId = userRepository.findCredentialsByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getUserId();

        return ResponseEntity.ok(dashboardService.getDashboardStatus(userId));
    }
}