package com.uteq.sgtic.controllers.admissions;

import com.uteq.sgtic.entities.AdmissionRequest;
import com.uteq.sgtic.projections.admissions.RequestManagementCoordinatorProjection;
import com.uteq.sgtic.services.admissions.IAdmissionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "http://localhost:4200")
public class AdmissionRequestController {
    @Autowired
    private IAdmissionRequestService service;

    @GetMapping
    public ResponseEntity<List<AdmissionRequest>> listarTodas() {
        try {
            return ResponseEntity.ok(service.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/coordinador/facultad/{idFacultad}")
    public ResponseEntity<List<RequestManagementCoordinatorProjection>> obtenerSolicitudesCoordinador(@PathVariable Integer idFacultad) {
        return ResponseEntity.ok(service.listarParaCoordinador(idFacultad));
    }

    @PutMapping("/aprobar/{id}")
    public ResponseEntity<?> aprobarSolicitud(@PathVariable Integer id) {
        try {
            service.aprobarSolicitud(id);
            return ResponseEntity.ok(Map.of("mensaje", "Solicitud aprobada con éxito"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/rechazar/{id}")
    public ResponseEntity<?> rechazarSolicitud(@PathVariable Integer id, @RequestBody Map<String, String> requestBody) {
        try {
            String motivo = requestBody.get("motivo");
            service.rechazarSolicitud(id, motivo);
            return ResponseEntity.ok(Map.of("mensaje", "Solicitud rechazada y correo enviado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
