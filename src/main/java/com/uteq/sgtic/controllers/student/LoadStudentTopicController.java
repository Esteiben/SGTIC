package com.uteq.sgtic.controllers.student;

import com.uteq.sgtic.config.security.JwtService;
import com.uteq.sgtic.dtos.student.LoadStudentTopicsDTO;
import com.uteq.sgtic.services.student.ILoadStudentTopicServices;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/temas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LoadStudentTopicController {

    private final ILoadStudentTopicServices loadStudentTopicServices;
    private final JwtService jwtService;

    @GetMapping("/disponibles")
    public ResponseEntity<?> getTemasDisponibles(
            @RequestParam Integer idOpcion,
            HttpServletRequest request
    ) {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Token no proporcionado");
            }

            String token = authHeader.substring(7);
            Integer idCarrera = jwtService.extractClaim(token, claims -> claims.get("idCareer", Integer.class));

            if (idCarrera == null) {
                return ResponseEntity.badRequest().body("El usuario no tiene una carrera asociada");
            }

            List<LoadStudentTopicsDTO> temas = loadStudentTopicServices.getTemasDisponibles(idCarrera, idOpcion);
            return ResponseEntity.ok(temas);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }
}