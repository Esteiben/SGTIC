package com.uteq.sgtic.controllers;

import com.uteq.sgtic.repository.CareerRepository;
import com.uteq.sgtic.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CareerController {

    private final CareerRepository careerRepository;
    private final FacultyRepository facultyRepository;

}
