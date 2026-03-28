package com.uteq.sgtic.controllers.InstitutionalStructurePackControllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.uteq.sgtic.dtos.institutionalstructure.FacultyDashboardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import com.uteq.sgtic.services.InstitutionalStructurePackServices.IFacultyDashboardServices;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/faculties")
@CrossOrigin(origins = "*")

public class FacultyDashboardControllers {

    private final IFacultyDashboardServices facultyDashboardServices;

    @GetMapping("/dashboard")
    public ResponseEntity<List<FacultyDashboardDTO>> getDashboard() {
        return ResponseEntity.ok(facultyDashboardServices.getFacultyDashboardData());
    }
}
