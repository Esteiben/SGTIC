package com.uteq.sgtic.controllers.student;

import com.uteq.sgtic.config.security.JwtService;
import com.uteq.sgtic.dtos.student.SaveTopicSelectionRequestDTO;
import com.uteq.sgtic.dtos.student.SaveTopicSelectionResponseDTO;
import com.uteq.sgtic.repository.UserRepository;
import com.uteq.sgtic.services.student.IStudentTopicSelectionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/temas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentTopicSelectionController {

    private final IStudentTopicSelectionService topicSelectionService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/seleccion")
    public ResponseEntity<?> saveSelection(
            @RequestBody SaveTopicSelectionRequestDTO requestBody,
            HttpServletRequest request,
            Principal principal
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

            String email = principal.getName();
            Integer userId = userRepository.findCredentialsByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                    .getUserId();

            SaveTopicSelectionResponseDTO response =
                    topicSelectionService.saveSelection(userId, idCarrera, requestBody);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }
}