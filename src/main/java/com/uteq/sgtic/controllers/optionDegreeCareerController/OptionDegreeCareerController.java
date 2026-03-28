package com.uteq.sgtic.controllers.optionDegreeCareerController;

import com.uteq.sgtic.projections.optionCareer.OptionCareerProjection;
import com.uteq.sgtic.services.optionDegreeCareerService.OptionDegreeCareerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coordinator/degree-options")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OptionDegreeCareerController {

    private final OptionDegreeCareerService service;
    @GetMapping("/career/{idCarrera}")
    public ResponseEntity<List<OptionCareerProjection>> getOptionsByCareer(@PathVariable Integer idCarrera) {
        List<OptionCareerProjection> options = service.getOptionsForCoordinator(idCarrera);
        return ResponseEntity.ok(options);
    }

    @PostMapping("/user/{idUsuario}/toggle/{idOpcion}")
    public ResponseEntity<Void> toggleOption(
            @PathVariable Integer idUsuario,
            @PathVariable Integer idOpcion,
            @RequestParam Boolean seleccionado
    ) {
        service.toggleOption(idUsuario, idOpcion, seleccionado);
        return ResponseEntity.ok().build();
    }
}