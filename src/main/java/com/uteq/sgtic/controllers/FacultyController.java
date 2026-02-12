package com.uteq.sgtic.controllers;

import com.uteq.sgtic.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyRepository facultyRepository;
}
