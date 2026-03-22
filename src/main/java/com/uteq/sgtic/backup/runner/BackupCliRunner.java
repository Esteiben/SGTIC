package com.uteq.sgtic.backup.runner;

import com.uteq.sgtic.backup.enums.BackupType;
import com.uteq.sgtic.backup.service.BackupManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "backup", name = "run", havingValue = "true")
public class BackupCliRunner implements CommandLineRunner {

    private final BackupManagerService backupManagerService;

    @Override
    public void run(String... args) {
        log.info("Iniciando proceso automático de respaldo desde Tarea Programada de Windows...");
        
        backupManagerService.recoverInterruptedExecutions();
        
        // El script por defecto ejecutará un FULL, podrías luego parametrizarlo
        backupManagerService.runBackup(BackupType.FULL, "SYSTEM", null);

        log.info("Proceso de respaldo finalizado. Cerrando aplicativo temporal...");
        System.exit(0);
    }
}