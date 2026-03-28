package com.uteq.sgtic.controllers.iaDuplicationController;

import com.uteq.sgtic.services.iaAssignmentService.AiDuplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/propuestas")
@CrossOrigin(origins = "*")
public class IaDuplicationController {
    private final AiDuplicationService iaDuplicationService;

    public IaDuplicationController(AiDuplicationService iaDuplicationService) {
        this.iaDuplicationService = iaDuplicationService;
    }

    @GetMapping("/{id}/verificar-duplicado")
    public ResponseEntity<String> verificarDuplicado(@PathVariable("id") Integer idTemaPropuesto) {
        try {
            String resultadoJson = iaDuplicationService.verificarDuplicidad(idTemaPropuesto);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(resultadoJson);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("{\"error\": \"Error interno al conectar con la Inteligencia Artificial.\"}");
        }
    }
}
