package com.uteq.sgtic.controllers.student;

import com.uteq.sgtic.dtos.student.DashboardStatusDTO;
import com.uteq.sgtic.repository.UserRepository;
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
    private final UserRepository userRepository;

    @GetMapping("/status")
    public ResponseEntity<DashboardStatusDTO> getStatus(
            @RequestParam("periodoId") Integer periodoId,
            Principal principal
    ) {
        String email = principal.getName();

        Integer userId = userRepository.findCredentialsByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getUserId();

        DashboardStatusDTO response = dashboardService.getDashboardStatus(userId, periodoId);
        return ResponseEntity.ok(response);
    }
}