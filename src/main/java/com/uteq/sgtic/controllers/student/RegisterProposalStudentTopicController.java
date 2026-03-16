package com.uteq.sgtic.controllers.student;

import com.uteq.sgtic.dtos.student.RegisterProposalStudentTopicDTO;
import com.uteq.sgtic.dtos.student.RegisterProposalStudentTopicResponseDTO;
import com.uteq.sgtic.services.student.IRegisterProposalStudentTopicServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/student/topic-proposals")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RegisterProposalStudentTopicController {

    private final IRegisterProposalStudentTopicServices registerProposalStudentTopicServices;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registrarPropuestaTema(
            @Valid @ModelAttribute RegisterProposalStudentTopicDTO dto
    ) {
        try {
            RegisterProposalStudentTopicResponseDTO response =
                    registerProposalStudentTopicServices.registrarPropuestaTema(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(error);

        } catch (DataAccessException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("mensaje", "Error al registrar la propuesta en la base de datos");
            error.put("detalle", e.getMostSpecificCause() != null
                    ? e.getMostSpecificCause().getMessage()
                    : e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("mensaje", "Error interno del servidor");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}