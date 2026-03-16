package com.uteq.sgtic.backup.controller;

import com.uteq.sgtic.backup.dto.BackupConfigRequestDTO;
import com.uteq.sgtic.backup.dto.BackupConfigResponseDTO;
import com.uteq.sgtic.backup.dto.BackupExecutionResponseDTO;
import com.uteq.sgtic.backup.dto.BackupMessageDTO;
import com.uteq.sgtic.backup.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para administrar la configuración de respaldos
 * y consultar el historial de ejecuciones.
 */
@RestController
@RequestMapping("/api/admin/backups")
@RequiredArgsConstructor
public class BackupController {

    private final BackupService backupService;

    /**
     * Obtiene la configuración activa.
     */
    @GetMapping("/config/active")
    public ResponseEntity<?> getActiveConfig() {
        try {
            BackupConfigResponseDTO response = backupService.getActiveConfig();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BackupMessageDTO(e.getMessage()));
        }
    }

    /**
     * Crea una configuración nueva.
     */
    @PostMapping("/config")
    public ResponseEntity<?> createConfig(@RequestBody BackupConfigRequestDTO request) {
        try {
            BackupConfigResponseDTO response = backupService.createConfig(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new BackupMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BackupMessageDTO("Error al crear la configuración: " + e.getMessage()));
        }
    }

    /**
     * Actualiza una configuración existente.
     */
    @PutMapping("/config/{id}")
    public ResponseEntity<?> updateConfig(@PathVariable Long id,
                                          @RequestBody BackupConfigRequestDTO request) {
        try {
            BackupConfigResponseDTO response = backupService.updateConfig(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new BackupMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BackupMessageDTO("Error al actualizar la configuración: " + e.getMessage()));
        }
    }

    /**
     * Lista el historial de ejecuciones.
     */
    @GetMapping("/executions")
    public ResponseEntity<List<BackupExecutionResponseDTO>> getExecutionHistory() {
        return ResponseEntity.ok(backupService.getExecutionHistory());
    }
}