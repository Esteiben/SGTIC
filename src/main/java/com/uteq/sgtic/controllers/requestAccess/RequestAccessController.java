package com.uteq.sgtic.controllers.requestAccess;

import com.uteq.sgtic.dtos.requestAccess.RequestAccessDTO;
import com.uteq.sgtic.services.requestAccess.IRequestAccessServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/public/request-access")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") 
public class RequestAccessController {

    private final IRequestAccessServices iRequestAccessServices;

    @PostMapping
    public ResponseEntity<?> requestAccess(@RequestBody RequestAccessDTO dto) {
        try {
            iRequestAccessServices.requestAccess(dto);
            
            return ResponseEntity.ok(Map.of("message", "Solicitud de acceso enviada correctamente."));
            
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            String errorMessage = cause.getMessage() != null ? cause.getMessage() : "Error interno al procesar la solicitud.";
            
            return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
        }
    }
}