package com.uteq.sgtic.controllers.requestAccess;

import com.uteq.sgtic.dtos.requestAccess.RequestAccessDTO;
import com.uteq.sgtic.services.requestAccess.IRequestAccessServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/request-access")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RequestAccessController {

    private final IRequestAccessServices requestAccessServices;

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody RequestAccessDTO dto) {
        try {
            requestAccessServices.createRequest(dto);
            return ResponseEntity.ok("Solicitud enviada correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error interno: " + e.getMessage());
        }
    }
}