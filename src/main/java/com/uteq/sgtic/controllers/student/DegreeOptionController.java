package com.uteq.sgtic.controllers.student;

import com.uteq.sgtic.config.security.JwtService;
import com.uteq.sgtic.services.student.IDegreeOptionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/degree-options")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DegreeOptionController {

    private final IDegreeOptionService degreeOptionService;
    private final JwtService jwtService;

    @GetMapping("/active")
    public ResponseEntity<?> getActiveOptions(HttpServletRequest request) {
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

            return ResponseEntity.ok(degreeOptionService.getActiveOptionsByCareer(idCarrera));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Token inválido: " + e.getMessage());
        }
    }
}