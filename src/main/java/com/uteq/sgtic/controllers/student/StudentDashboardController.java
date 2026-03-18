package com.uteq.sgtic.controllers.student;

import com.uteq.sgtic.dtos.student.DashboardStatusDTO;
import com.uteq.sgtic.services.impl.student.StudentDashboardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/dashboard")
@RequiredArgsConstructor
public class StudentDashboardController {

    private final StudentDashboardServiceImpl dashboardService;

    @GetMapping("/status")
    public ResponseEntity<DashboardStatusDTO> getStatus(
            @RequestParam Integer periodoId,
            @RequestParam Integer estudianteId) {
        
        DashboardStatusDTO status = dashboardService.getDashboardStatus(estudianteId, periodoId);
        return ResponseEntity.ok(status);
    }
}