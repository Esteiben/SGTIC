package com.uteq.sgtic.backup.service.strategy;

import com.uteq.sgtic.backup.entity.BackupConfig;
import com.uteq.sgtic.backup.entity.BackupExecution;
import com.uteq.sgtic.backup.enums.BackupType;
import com.uteq.sgtic.backup.repository.BackupExecutionRepository;
import com.uteq.sgtic.backup.service.GoogleDriveStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FullBackupStrategyImpl implements IBackupStrategy {

    private final BackupExecutionRepository executionRepository;
    private final GoogleDriveStorageService driveStorageService;

    // --- CREDENCIALES BASE SECUNDARIA (Inyectadas desde properties) ---
    @Value("${backup.restore.host:localhost}")
    private String restoreHost;

    @Value("${backup.restore.port:5432}")
    private Integer restorePort;

    @Value("${backup.restore.db-name:sgtic_secundaria}")
    private String restoreDbName;

    @Value("${backup.restore.user:postgres}")
    private String restoreUser;

    @Value("${backup.restore.password:}")
    private String restorePassword;

    @Override
    public boolean supports(BackupType type) { return type == BackupType.FULL; }

    @Override
    public void executeBackup(BackupConfig config, String triggeredBy, Integer idUsuario) {
        Path backupDir = Paths.get(config.getLocalPath());
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String baseFileName = config.getFilePrefix() + "_" + timestamp;
        
        Path tempDbFile = backupDir.resolve(baseFileName + ".backup");
        Path finalZipFile = backupDir.resolve(baseFileName + ".zip");
        Path logFile = backupDir.resolve(baseFileName + ".log");

        Long executionId = executionRepository.iniciarEjecucion(
                config.getId(), idUsuario, "FULL", triggeredBy, finalZipFile.getFileName().toString(), finalZipFile.toString()
        );

        try {
            Files.createDirectories(backupDir);

            List<String> command = new ArrayList<>();
            command.add(StringUtils.hasText(config.getPgDumpPath()) ? config.getPgDumpPath() : "pg_dump");
            command.add("-h"); command.add(config.getDatabaseHost());
            command.add("-p"); command.add(String.valueOf(config.getDatabasePort()));
            command.add("-U"); command.add(config.getDatabaseUser());
            command.add("-F"); command.add("c"); 
            command.add("-f"); command.add(tempDbFile.toString());
            command.add(config.getDatabaseName());

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            pb.redirectOutput(logFile.toFile());
            if (StringUtils.hasText(config.getPgpassFilePath())) {
                pb.environment().put("PGPASSFILE", config.getPgpassFilePath());
            }

            Process process = pb.start();
            boolean finished = process.waitFor(30, TimeUnit.MINUTES);

            if (!finished || process.exitValue() != 0) {
                if (!finished) process.destroyForcibly();
                throw new RuntimeException("Error ejecutando pg_dump.");
            }

            compressToZip(tempDbFile, finalZipFile);

            long sizeBytes = Files.size(finalZipFile);
            executionRepository.finalizarEjecucion(executionId, "SUCCESS", 0, sizeBytes, "Respaldo FULL y compresión a ZIP generados correctamente.");

            BackupExecution execObj = executionRepository.findById(executionId).orElseThrow();
            driveStorageService.uploadBackupIfConfigured(config, execObj, finalZipFile);

        } catch (Exception e) {
            log.error("Error en FULL backup", e);
            executionRepository.finalizarEjecucion(executionId, "FAILED", -1, 0L, "Fallo en ejecución: " + e.getMessage());
        }
    }

    @Override
    public void restoreBackup(BackupConfig config, Long executionId, Integer idUsuarioAdmin) {
        BackupExecution execution = executionRepository.findById(executionId).orElseThrow();
        Path zipFilePath = Paths.get(execution.getFilePath());
        
        zipFilePath = driveStorageService.downloadBackupIfMissing(config, execution, zipFilePath);

        Path extractedBackupFile = null;
        try {
            extractedBackupFile = extractFromZip(zipFilePath);

            String pgRestorePath = config.getPgDumpPath().replace("pg_dump.exe", "pg_restore.exe");
            if (pgRestorePath.equals(config.getPgDumpPath())) pgRestorePath = "pg_restore";

            List<String> command = new ArrayList<>();
            command.add(pgRestorePath);
            command.add("-h"); command.add(restoreHost);
            command.add("-p"); command.add(String.valueOf(restorePort));
            command.add("-U"); command.add(restoreUser);
            command.add("-d"); command.add(restoreDbName);
            command.add("-v"); 
            command.add("-c");
            command.add("--if-exists");
            command.add(extractedBackupFile.toString());

            ProcessBuilder pb = new ProcessBuilder(command);
            
            if (StringUtils.hasText(restorePassword)) {
                pb.environment().put("PGPASSWORD", restorePassword);
            }
            pb.environment().put("PGSSLMODE", "require");

            pb.redirectErrorStream(true);
            pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            
            Process process = pb.start();
            boolean finished = process.waitFor(45, TimeUnit.MINUTES);

            if (!finished || process.exitValue() != 0) throw new RuntimeException("Error ejecutando pg_restore en la base secundaria.");

            executionRepository.registrarRestauracion(executionId, idUsuarioAdmin, "Restauración exitosa a la Base Secundaria (DB: " + restoreDbName + ") a partir del archivo " + zipFilePath.getFileName());

        } catch (Exception e) {
            log.error("Error restaurando la base de datos", e);
            throw new IllegalStateException("Falló la restauración: " + e.getMessage());
        } finally {
            if (extractedBackupFile != null) {
                try { Files.deleteIfExists(extractedBackupFile); } catch (IOException ignored) {}
            }
        }
    }

    private void compressToZip(Path sourceFile, Path zipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile.toFile());
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(sourceFile.toFile())) {
            ZipEntry zipEntry = new ZipEntry(sourceFile.getFileName().toString());
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[8192];
            int length;
            while ((length = fis.read(bytes)) >= 0) zos.write(bytes, 0, length);
        }
        Files.deleteIfExists(sourceFile);
    }

    private Path extractFromZip(Path zipFile) throws IOException {
        Path destDir = zipFile.getParent();
        Path extractedFile = null;
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile.toFile()))) {
            ZipEntry zipEntry = zis.getNextEntry();
            if (zipEntry != null) {
                extractedFile = destDir.resolve(zipEntry.getName());
                try (FileOutputStream fos = new FileOutputStream(extractedFile.toFile())) {
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = zis.read(buffer)) > 0) fos.write(buffer, 0, len);
                }
            }
        }
        if (extractedFile == null) throw new IOException("El archivo ZIP está vacío o dañado.");
        return extractedFile;
    }
}