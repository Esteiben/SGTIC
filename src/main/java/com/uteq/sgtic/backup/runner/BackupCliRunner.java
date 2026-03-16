package com.uteq.sgtic.backup.runner;

import com.uteq.sgtic.backup.service.BackupService;
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

    private final BackupService backupService;

    @Override
    public void run(String... args) {
        log.info("Iniciando proceso automático de respaldo...");

        // Limpia ejecuciones previas que quedaron colgadas
        backupService.recoverInterruptedExecutions();

        int exitCode = backupService.runFullBackup("SYSTEM", null);

        log.info("Proceso de respaldo finalizado con código {}", exitCode);

        System.exit(exitCode);
    }
}