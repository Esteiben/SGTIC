package com.uteq.sgtic.controllers.student;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uteq.sgtic.dtos.student.mainDashboard.DashboardStatusDTO;
import com.uteq.sgtic.services.student.IStudentDashboardService;
import com.uteq.sgtic.services.student.selectTopic.IProcessSetupService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class StudentDashboardController {

    private final IStudentDashboardService dashboardService;
    private final IProcessSetupService processSetupService;

    @GetMapping("/status")
    public ResponseEntity<DashboardStatusDTO> getStatus(
            @RequestParam Integer periodoId,
            Authentication authentication
    ) {
        Integer estudianteIdReal = processSetupService.getIdEstudianteByUsername(authentication.getName());
        
        DashboardStatusDTO status = dashboardService.getDashboardStatus(estudianteIdReal, periodoId);
        return ResponseEntity.ok(status);
    }
}