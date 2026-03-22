package com.uteq.sgtic.controllers.student.selectTopic;

import com.uteq.sgtic.dtos.student.selectTopic.DegreeOptionDTO;
import com.uteq.sgtic.services.student.selectTopic.IProcessSetupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/degree-options")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DegreeOptionController {

    private final IProcessSetupService processSetupService;

    @GetMapping("/by-period")
    public ResponseEntity<List<DegreeOptionDTO>> getOptionsByPeriod(
            @RequestParam Integer idPeriodo,
            Authentication authentication
    ) {
        Integer idEstudiante = processSetupService.getIdEstudianteByUsername(authentication.getName());

        return ResponseEntity.ok(
                processSetupService.getOptionsByStudentPeriod(idPeriodo, idEstudiante)
        );
    }
}