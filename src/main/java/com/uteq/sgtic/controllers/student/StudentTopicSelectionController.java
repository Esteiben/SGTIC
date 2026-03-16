package com.uteq.sgtic.controllers.student;

import com.uteq.sgtic.config.security.JwtService;
import com.uteq.sgtic.dtos.student.*;
import com.uteq.sgtic.repository.UserRepository;
import com.uteq.sgtic.services.student.IStudentTopicSelectionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/temas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentTopicSelectionController {

    private final IStudentTopicSelectionService topicSelectionService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @GetMapping("/estado")
    public ResponseEntity<?> getSelectionStatus(
            @RequestParam Integer idPeriodo,
            Principal principal
    ) {
        try {
            Integer userId = getUserIdFromPrincipal(principal);

            TopicSelectionStatusDTO response =
                    topicSelectionService.getSelectionStatus(userId, idPeriodo);

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

    @PostMapping("/seleccion")
    public ResponseEntity<?> saveSelection(
            @RequestBody SaveTopicSelectionRequestDTO requestBody,
            HttpServletRequest request,
            Principal principal
    ) {
        try {
            Integer idCarrera = getCareerIdFromToken(request);
            Integer userId = getUserIdFromPrincipal(principal);

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

    @PostMapping(
            value = "/propuesta-estudiante",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> registerProposal(
            @ModelAttribute RegisterProposalStudentTopicRequestDTO requestBody,
            HttpServletRequest request,
            Principal principal
    ) {
        try {
            Integer idCarrera = getCareerIdFromToken(request);
            Integer userId = getUserIdFromPrincipal(principal);

            RegisterProposalStudentTopicResponseDTO response =
                    topicSelectionService.registerProposal(userId, idCarrera, requestBody);

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

    @GetMapping("/propuestas-estudiante")
    public ResponseEntity<?> getStudentProposals(
            @RequestParam Integer idPeriodo,
            Principal principal
    ) {
        try {
            Integer userId = getUserIdFromPrincipal(principal);

            List<StudentProposalSummaryDTO> response =
                    topicSelectionService.getStudentProposals(userId, idPeriodo);

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

    @GetMapping("/propuestas-estudiante/{idPropuesta}/historial")
    public ResponseEntity<?> getStudentProposalHistory(
            @PathVariable Integer idPropuesta,
            Principal principal
    ) {
        try {
            Integer userId = getUserIdFromPrincipal(principal);

            List<StudentProposalHistoryItemDTO> response =
                    topicSelectionService.getStudentProposalHistory(userId, idPropuesta);

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

    @PutMapping(
            value = "/propuestas-estudiante/{idPropuesta}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> updateStudentProposal(
            @PathVariable Integer idPropuesta,
            @ModelAttribute UpdateStudentProposalRequestDTO requestBody,
            Principal principal
    ) {
        try {
            Integer userId = getUserIdFromPrincipal(principal);

            UpdateStudentProposalResponseDTO response =
                    topicSelectionService.updateStudentProposal(userId, idPropuesta, requestBody);

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

    private Integer getCareerIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token no proporcionado.");
        }

        String token = authHeader.substring(7);
        Integer idCarrera = jwtService.extractClaim(token, claims -> claims.get("idCareer", Integer.class));

        if (idCarrera == null) {
            throw new IllegalArgumentException("El usuario no tiene una carrera asociada.");
        }

        return idCarrera;
    }

    private Integer getUserIdFromPrincipal(Principal principal) {
        String email = principal.getName();

        return userRepository.findCredentialsByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."))
                .getUserId();
    }
}