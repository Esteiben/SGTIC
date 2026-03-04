package com.uteq.sgtic.controllers.studentProcessSetup;

import com.uteq.sgtic.dtos.DegreeOptionDTO;
import com.uteq.sgtic.services.IDegreeOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/degree-options")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DegreeOptionController {

    private final IDegreeOptionService degreeOptionService;

    @GetMapping("/active")
    public ResponseEntity<List<DegreeOptionDTO>> getActiveOptions() {
        return ResponseEntity.ok(degreeOptionService.getActiveOptions());
    }
}