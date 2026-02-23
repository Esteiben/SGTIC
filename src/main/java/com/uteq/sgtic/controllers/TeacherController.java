package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.TeacherResponseDTO;
import com.uteq.sgtic.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/docentes")
@CrossOrigin(origins = "http://localhost:4200")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/buscar")
    public List<TeacherResponseDTO> search(@RequestParam(required = false, defaultValue = "") String termino) {
        return teacherService.searchTeachers(termino);
    }
}