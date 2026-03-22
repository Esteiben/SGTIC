package com.uteq.sgtic.backup.controller;

import com.uteq.sgtic.backup.dto.*;
import com.uteq.sgtic.backup.enums.BackupType;
import com.uteq.sgtic.backup.service.BackupManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/backups")
@RequiredArgsConstructor
public class BackupController {

    private final BackupManagerService backupManagerService;

    @GetMapping("/config/active")
    public ResponseEntity<?> getActiveConfig() {
        try { return ResponseEntity.ok(backupManagerService.getActiveConfig()); } 
        catch (Exception e) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BackupMessageDTO(e.getMessage())); }
    }

    @PostMapping("/config")
    public ResponseEntity<?> createConfig(@RequestBody BackupConfigRequestDTO request) {
        try { return ResponseEntity.status(HttpStatus.CREATED).body(backupManagerService.createConfig(request)); } 
        catch (Exception e) { return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BackupMessageDTO(e.getMessage())); }
    }

    @PutMapping("/config/{id}")
    public ResponseEntity<?> updateConfig(@PathVariable Long id, @RequestBody BackupConfigRequestDTO request) {
        try { return ResponseEntity.ok(backupManagerService.updateConfig(id, request)); } 
        catch (Exception e) { return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BackupMessageDTO(e.getMessage())); }
    }

    @GetMapping("/executions")
    public ResponseEntity<List<BackupExecutionResponseDTO>> getExecutionHistory() {
        return ResponseEntity.ok(backupManagerService.getExecutionHistory());
    }

    // Nuevo endpoint para que Angular mande un backup manual (eligiendo el tipo)
    @PostMapping("/run")
    public ResponseEntity<?> runBackupManual(@RequestParam String type, @RequestParam Integer adminId) {
        try {
            BackupType backupType = BackupType.valueOf(type.toUpperCase());
            backupManagerService.runBackup(backupType, "ADMIN_MANUAL", adminId);
            return ResponseEntity.ok(new BackupMessageDTO("Backup " + type + " iniciado correctamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BackupMessageDTO(e.getMessage()));
        }
    }

    // Nuevo endpoint de RESTAURACIÓN
    @PostMapping("/executions/{id}/restore")
    public ResponseEntity<?> restoreDatabase(@PathVariable Long id, @RequestParam Integer adminId) {
        try {
            backupManagerService.restoreBackup(id, adminId);
            return ResponseEntity.ok(new BackupMessageDTO("Base de datos restaurada correctamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BackupMessageDTO("Error de restauración: " + e.getMessage()));
        }
    }

    // Endpoint para sincronización desde la interfaz
    @PostMapping("/sync")
    public ResponseEntity<?> syncToSecondaryDatabase(
            @RequestParam Integer adminId, 
            @RequestParam(required = false, defaultValue = "") String cronKey) {
            
        if (!cronKey.equals("SGTIC_SECRET_CRON_2026")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new BackupMessageDTO("Acceso denegado. Llave de tarea programada inválida."));
        }

        try {
            backupManagerService.syncToSecondary(adminId);
            return ResponseEntity.ok(new BackupMessageDTO("Sincronización automática completada con éxito."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BackupMessageDTO("Error en sincronización: " + e.getMessage()));
        }
    }
}