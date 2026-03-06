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

    @GetMapping("/coordinador/{idUsuario}")
    public ResponseEntity<List<RequestManagementCoordinatorProjection>> obtenerSolicitudesCoordinador(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(service.listarParaCoordinador(idUsuario));
    }

    @PutMapping("/aprobar/{id}")
    public ResponseEntity<?> aprobarSolicitud(@PathVariable("id") Integer id) { // Aseguramos el mapeo del ID
        try {
            service.aprobarSolicitud(id);
            return ResponseEntity.ok(Map.of("mensaje", "Solicitud aprobada: Credenciales enviadas al estudiante"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/rechazar/{id}")
    public ResponseEntity<?> rechazarSolicitud(@PathVariable("id") Integer id, @RequestBody Map<String, String> requestBody) {
        try {
            String motivo = requestBody.get("motivo");

            if (motivo == null || motivo.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Debe proporcionar un motivo para el rechazo"));
            }

            service.rechazarSolicitud(id, motivo);
            return ResponseEntity.ok(Map.of("mensaje", "Solicitud rechazada: Motivo enviado por correo"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}