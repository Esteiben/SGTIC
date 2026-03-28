package com.uteq.sgtic.controllers;

import com.uteq.sgtic.entities.AuditoriaSistema;
import com.uteq.sgtic.services.AuditoriaSistemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@CrossOrigin(origins = "*")
public class AuditoriaSistemaController {

    @Autowired
    private AuditoriaSistemaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<AuditoriaSistema>> listarAuditoria() {
        List<AuditoriaSistema> historial = auditoriaService.obtenerHistorialAuditoria();
        return ResponseEntity.ok(historial);
    }
}