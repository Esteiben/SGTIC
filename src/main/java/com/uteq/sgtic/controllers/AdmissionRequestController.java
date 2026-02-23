package com.uteq.sgtic.controllers;
import com.uteq.sgtic.entities.AdmissionRequest;
import com.uteq.sgtic.services.IAdmissionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.uteq.sgtic.projections.RequestManagementCoordinatorProjection;
import java.util.Map;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "http://localhost:4200")
public class AdmissionRequestController {

    @Autowired
    private IAdmissionRequestService service;

    @GetMapping
    public ResponseEntity<List<AdmissionRequest>> listarTodas() {
        try {
            List<AdmissionRequest> lista = service.findAll();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/coordinador/carrera/{idCarrera}")
    public ResponseEntity<List<RequestManagementCoordinatorProjection>> obtenerSolicitudesCoordinador(@PathVariable Integer idCarrera) {
        List<RequestManagementCoordinatorProjection> lista = service.listarParaCoordinador(idCarrera);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @PutMapping("/aprobar/{id}")
    public ResponseEntity<?> aprobarSolicitud(@PathVariable Integer id) {
        try {
            service.aprobarSolicitud(id);
            return ResponseEntity.ok().body(Map.of("mensaje", "Solicitud aprobada con éxito"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/rechazar/{id}")
    public ResponseEntity<?> rechazarSolicitud(@PathVariable Integer id, @RequestBody Map<String, String> requestBody) {
        try {
            String motivo = requestBody.get("motivo");
            service.rechazarSolicitud(id, motivo);
            return ResponseEntity.ok().body(Map.of("mensaje", "Solicitud rechazada"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}