package com.uteq.sgtic.backup.controller;

import com.uteq.sgtic.backup.service.BackupRestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/backup")
@CrossOrigin(origins = "*") // Ajusta esto según tus políticas de CORS
public class BackupRestoreController {

    @Autowired
    private BackupRestoreService backupRestoreService;

    // Solo el administrador debería poder hacer esto. Si usas Spring Security,
    // puedes añadir algo como @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/emergency-restore")
    public ResponseEntity<Map<String, Object>> restoreDatabase(@RequestParam String fileName) {
        Map<String, Object> response = new HashMap<>();
        
        // Validación básica
        if (fileName == null || !fileName.endsWith(".zip")) {
            response.put("success", false);
            response.put("message", "El archivo debe ser un .zip válido.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Ejecutar el orquestador
        boolean exito = backupRestoreService.ejecutarRecuperacionTotal(fileName);

        if (exito) {
            response.put("success", true);
            response.put("message", "Base de datos principal (sgtic) restaurada con éxito. Reinicie el servidor en modo normal.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("success", false);
            response.put("message", "Fallo al restaurar la base de datos. Revise los logs del servidor.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}