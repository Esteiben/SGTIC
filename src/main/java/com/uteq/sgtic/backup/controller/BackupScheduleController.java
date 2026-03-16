package com.uteq.sgtic.backup.controller;

import com.uteq.sgtic.backup.dto.BackupMessageDTO;
import com.uteq.sgtic.backup.dto.BackupScheduleRequestDTO;
import com.uteq.sgtic.backup.dto.BackupScheduleResponseDTO;
import com.uteq.sgtic.backup.service.BackupScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para administrar tareas programadas de backup.
 */
@RestController
@RequestMapping("/api/admin/backup-schedules")
@RequiredArgsConstructor
public class BackupScheduleController {

    private final BackupScheduleService backupScheduleService;

    @GetMapping
    public ResponseEntity<List<BackupScheduleResponseDTO>> getAll() {
        return ResponseEntity.ok(backupScheduleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(backupScheduleService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BackupMessageDTO(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BackupScheduleRequestDTO request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(backupScheduleService.create(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new BackupMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BackupMessageDTO("Error al crear la tarea: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody BackupScheduleRequestDTO request) {
        try {
            return ResponseEntity.ok(backupScheduleService.update(id, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new BackupMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BackupMessageDTO("Error al actualizar la tarea: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/sync")
    public ResponseEntity<?> sync(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(backupScheduleService.sync(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BackupMessageDTO("Error al sincronizar la tarea: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/run")
    public ResponseEntity<?> runNow(@PathVariable Long id) {
        try {
            backupScheduleService.runNow(id);
            return ResponseEntity.ok(new BackupMessageDTO("Tarea lanzada correctamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BackupMessageDTO("Error al ejecutar la tarea: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            backupScheduleService.delete(id);
            return ResponseEntity.ok(new BackupMessageDTO("Tarea eliminada correctamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BackupMessageDTO("Error al eliminar la tarea: " + e.getMessage()));
        }
    }
}