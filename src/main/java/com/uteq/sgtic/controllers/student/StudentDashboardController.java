package com.uteq.sgtic.controllers.student;

import com.uteq.sgtic.dtos.student.mainDashboard.DashboardStatusDTO;
import com.uteq.sgtic.services.student.IStudentDashboardService;
import com.uteq.sgtic.services.student.selectTopic.IProcessSetupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Lo agregamos para mantener consistencia con tu frontend
public class StudentDashboardController {

    private final IStudentDashboardService dashboardService;
    private final IProcessSetupService processSetupService; // Lo usamos para extraer el ID seguro

    @GetMapping("/status")
    public ResponseEntity<DashboardStatusDTO> getStatus(
            @RequestParam Integer periodoId,
            Authentication authentication // Spring inyecta al usuario logueado automáticamente
    ) {
        // Obtenemos el ID real del estudiante basado en su token JWT
        Integer estudianteIdReal = processSetupService.getIdEstudianteByUsername(authentication.getName());
        
        DashboardStatusDTO status = dashboardService.getDashboardStatus(estudianteIdReal, periodoId);
        return ResponseEntity.ok(status);
    }
}