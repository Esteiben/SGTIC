package com.uteq.sgtic.controllers.admissions;



import com.uteq.sgtic.services.admissions.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping("/sesiones")
    public ResponseEntity<List<Map<String, Object>>> obtenerSesiones() {
        return ResponseEntity.ok(auditoriaService.listarSesiones());
    }

    @PostMapping("/sesiones/{idUsuario}/desconectar")
    public ResponseEntity<?> desconectarUsuario(@PathVariable Long idUsuario) {
        try {
            auditoriaService.expulsarUsuario(idUsuario);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario desconectado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}