package com.uteq.sgtic.backup.service;

import com.uteq.sgtic.backup.dto.BackupConfigRequestDTO;
import com.uteq.sgtic.backup.dto.BackupConfigResponseDTO;
import com.uteq.sgtic.backup.dto.BackupExecutionResponseDTO;
import com.uteq.sgtic.backup.entity.BackupConfig;
import com.uteq.sgtic.backup.entity.BackupExecution;
import com.uteq.sgtic.backup.enums.BackupStatus;
import com.uteq.sgtic.backup.repository.BackupConfigRepository;
import com.uteq.sgtic.backup.repository.BackupExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackupService {

    private final BackupConfigRepository backupConfigRepository;
    private final BackupExecutionRepository backupExecutionRepository;
    private final GoogleDriveStorageService googleDriveStorageService;

    public BackupConfigResponseDTO getActiveConfig() {
        BackupConfig config = backupConfigRepository.findFirstByActiveTrue()
                .orElseThrow(() -> new IllegalStateException("No existe una configuración activa."));

        return mapConfigToResponse(config);
    }

    public BackupConfigResponseDTO createConfig(BackupConfigRequestDTO request) {
        validateRequest(request);

        BackupConfig config = BackupConfig.builder()
            .createdBy(request.getUserId())
            .updatedBy(null)
            .active(request.getActive() != null ? request.getActive() : true)
            .pgDumpPath(request.getPgDumpPath())
            .localPath(request.getLocalPath())
            .filePrefix(request.getFilePrefix())
            .databaseHost(request.getDatabaseHost())
            .databasePort(request.getDatabasePort())
            .databaseName(request.getDatabaseName())
            .databaseUser(request.getDatabaseUser())
            .pgpassFilePath(request.getPgpassFilePath())
            .cleanupEnabled(request.getCleanupEnabled() != null ? request.getCleanupEnabled() : true)
            .retentionDays(request.getRetentionDays() != null ? request.getRetentionDays() : 30)
            .driveEnabled(request.getDriveEnabled() != null ? request.getDriveEnabled() : false)
            .driveAccountId(request.getDriveAccountId())
            .driveFolderId(request.getDriveFolderId())
            .build();

        config = backupConfigRepository.save(config);
        return mapConfigToResponse(config);
    }

    public BackupConfigResponseDTO updateConfig(Long id, BackupConfigRequestDTO request) {
        validateRequest(request);

        BackupConfig config = backupConfigRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No se encontró la configuración con id " + id));

        config.setUpdatedBy(request.getUserId());
        config.setActive(request.getActive() != null ? request.getActive() : config.getActive());
        config.setPgDumpPath(request.getPgDumpPath());
        config.setLocalPath(request.getLocalPath());
        config.setFilePrefix(request.getFilePrefix());
        config.setDatabaseHost(request.getDatabaseHost());
        config.setDatabasePort(request.getDatabasePort());
        config.setDatabaseName(request.getDatabaseName());
        config.setDatabaseUser(request.getDatabaseUser());
        config.setPgpassFilePath(request.getPgpassFilePath());
        config.setCleanupEnabled(request.getCleanupEnabled() != null ? request.getCleanupEnabled() : config.getCleanupEnabled());
        config.setRetentionDays(request.getRetentionDays() != null ? request.getRetentionDays() : config.getRetentionDays());

        config.setDriveEnabled(request.getDriveEnabled() != null ? request.getDriveEnabled() : config.getDriveEnabled());
        config.setDriveAccountId(request.getDriveAccountId());
        config.setDriveFolderId(request.getDriveFolderId());

        config = backupConfigRepository.save(config);
        return mapConfigToResponse(config);
    }

    public List<BackupExecutionResponseDTO> getExecutionHistory() {
        return backupExecutionRepository.findAllByOrderByStartedAtDesc()
                .stream()
                .map(this::mapExecutionToResponse)
                .toList();
    }

    public void recoverInterruptedExecutions() {
        List<BackupExecution> runningExecutions =
                backupExecutionRepository.findAllByStatusOrderByStartedAtDesc(BackupStatus.RUNNING);

        if (runningExecutions.isEmpty()) {
            return;
        }

        for (BackupExecution execution : runningExecutions) {
            execution.setStatus(BackupStatus.FAILED);
            execution.setFinishedAt(LocalDateTime.now());
            execution.setExitCode(-2);

            if (execution.getMessage() == null
                    || execution.getMessage().isBlank()
                    || "Proceso iniciado.".equalsIgnoreCase(execution.getMessage())
                    || "Proceso en ejecución.".equalsIgnoreCase(execution.getMessage())) {
                execution.setMessage("Proceso interrumpido o finalización inesperada.");
            }
        }

        backupExecutionRepository.saveAll(runningExecutions);
        backupExecutionRepository.flush();
    }

    public int runFullBackup(String triggeredBy, Integer idUsuario) {
        BackupConfig config = backupConfigRepository.findFirstByActiveTrue()
                .orElseThrow(() -> new IllegalStateException("No existe una configuración activa en backup_config."));

        Path outputFile = null;
        Path processLogFile = null;
        BackupExecution execution = null;

        try {
            Path backupDir = Paths.get(config.getLocalPath());
            Files.createDirectories(backupDir);

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String fileName = config.getFilePrefix() + "_" + timestamp + ".backup";
            outputFile = backupDir.resolve(fileName);

            String logFileName = config.getFilePrefix() + "_" + timestamp + ".log";
            processLogFile = backupDir.resolve(logFileName);

            execution = BackupExecution.builder()
                    .backupConfigId(config.getId())
                    .idUsuario(idUsuario)
                    .status(BackupStatus.RUNNING)
                    .backupType("FULL")
                    .triggeredBy(triggeredBy)
                    .startedAt(LocalDateTime.now())
                    .fileName(fileName)
                    .filePath(outputFile.toString())
                    .message("Proceso en ejecución.")
                    .fileDeleted(false)
                    .build();

            execution = backupExecutionRepository.saveAndFlush(execution);

            List<String> command = buildPgDumpCommand(config, outputFile);

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            processBuilder.redirectOutput(processLogFile.toFile());

            if (StringUtils.hasText(config.getPgpassFilePath())) {
                processBuilder.environment().put("PGPASSFILE", config.getPgpassFilePath());
            }

            processBuilder.environment().put("PGSSLMODE", "require");

            Process process = processBuilder.start();

            boolean finished = process.waitFor(30, TimeUnit.MINUTES);
            if (!finished) {
                process.destroyForcibly();
                return failExecution(
                        execution,
                        outputFile,
                        -1,
                        "El proceso excedió el tiempo máximo de 30 minutos.\n"
                                + truncate(readLogFile(processLogFile), 4000)
                );
            }

            int exitCode = process.exitValue();
            String processOutput = readLogFile(processLogFile);

            if (exitCode != 0) {
                return failExecution(
                        execution,
                        outputFile,
                        exitCode,
                        "pg_dump terminó con error: " + truncate(processOutput, 4000)
                );
            }

            if (!Files.exists(outputFile)) {
                return failExecution(
                        execution,
                        outputFile,
                        exitCode,
                        "pg_dump terminó sin error, pero no se encontró el archivo generado.\n"
                                + truncate(processOutput, 4000)
                );
            }

            long fileSize = Files.size(outputFile);

            execution.setStatus(BackupStatus.SUCCESS);
            execution.setFinishedAt(LocalDateTime.now());
            execution.setExitCode(exitCode);
            execution.setFileSizeBytes(fileSize);

            String successMessage = "Respaldo generado correctamente.";

            try {
                RetentionResult retentionResult = applyRetentionPolicy(config);
                if (retentionResult.deletedCount() > 0) {
                    successMessage += " Limpieza automática: " + retentionResult.deletedCount() + " archivo(s) eliminados.";
                }
            } catch (Exception retentionEx) {
                log.error("El backup fue exitoso, pero la limpieza automática falló", retentionEx);
                successMessage += " El backup fue exitoso, pero la limpieza automática falló.";
            }

            execution.setMessage(successMessage);
            backupExecutionRepository.saveAndFlush(execution);

            try {
                googleDriveStorageService.uploadBackupIfConfigured(config, execution, outputFile);
            } catch (Exception driveEx) {
                log.error("El backup local fue exitoso, pero la subida a Google Drive falló", driveEx);
            }

            return 0;

        } catch (Exception e) {
            log.error("Error ejecutando backup", e);

            if (execution != null) {
                return failExecution(
                        execution,
                        outputFile,
                        -1,
                        "Excepción durante la ejecución: " + truncate(e.toString(), 4000)
                );
            }

            return -1;
        }
    }

    private RetentionResult applyRetentionPolicy(BackupConfig config) {
        if (config.getCleanupEnabled() == null || !config.getCleanupEnabled()) {
            return new RetentionResult(0);
        }

        int retentionDays = config.getRetentionDays() != null ? config.getRetentionDays() : 30;
        if (retentionDays < 1) {
            retentionDays = 30;
        }

        LocalDateTime cutoff = LocalDateTime.now().minusDays(retentionDays);
        List<BackupExecution> candidates = backupExecutionRepository.findRetentionCandidates(cutoff);

        int deletedCount = 0;

        for (BackupExecution candidate : candidates) {
            boolean deleted = deleteBackupAndLog(candidate);
            if (deleted) {
                deletedCount++;
            }
        }

        // Refuerzo final
        backupExecutionRepository.flush();

        return new RetentionResult(deletedCount);
    }

    private boolean deleteBackupAndLog(BackupExecution execution) {
        Path backupPath = Paths.get(execution.getFilePath());
        Path logPath = resolveLogPath(execution.getFilePath());

        boolean backupDeletedOrMissing = false;
        StringBuilder cleanupMsg = new StringBuilder();

        try {
            if (Files.exists(backupPath)) {
                Files.delete(backupPath);
                cleanupMsg.append("Archivo backup eliminado. ");
            } else {
                cleanupMsg.append("El archivo backup ya no existía. ");
            }
            backupDeletedOrMissing = true;
        } catch (Exception e) {
            cleanupMsg.append("No se pudo eliminar el archivo backup: ")
                    .append(e.getMessage())
                    .append(". ");
        }

        try {
            if (logPath != null && Files.exists(logPath)) {
                Files.delete(logPath);
                cleanupMsg.append("Archivo log eliminado.");
            } else {
                cleanupMsg.append("El archivo log no existía.");
            }
        } catch (Exception e) {
            cleanupMsg.append("No se pudo eliminar el archivo log: ")
                    .append(e.getMessage())
                    .append(".");
        }

        if (backupDeletedOrMissing) {
            execution.setFileDeleted(true);
            execution.setDeletedAt(LocalDateTime.now());
            execution.setDeletionReason("RETENTION_POLICY");
            execution.setCleanupMessage(truncate(cleanupMsg.toString(), 4000));

            // Importante: forzar escritura inmediata
            backupExecutionRepository.saveAndFlush(execution);
            return true;
        } else {
            execution.setCleanupMessage(truncate(cleanupMsg.toString(), 4000));

            // Importante: forzar escritura inmediata
            backupExecutionRepository.saveAndFlush(execution);
            return false;
        }
    }

    private Path resolveLogPath(String filePath) {
        if (!StringUtils.hasText(filePath)) {
            return null;
        }

        if (filePath.endsWith(".backup")) {
            return Paths.get(filePath.replace(".backup", ".log"));
        }

        return null;
    }

    private List<String> buildPgDumpCommand(BackupConfig config, Path outputFile) {
        String executable = StringUtils.hasText(config.getPgDumpPath())
                ? config.getPgDumpPath()
                : "pg_dump";

        if (StringUtils.hasText(config.getPgDumpPath()) && !Files.exists(Paths.get(config.getPgDumpPath()))) {
            throw new IllegalStateException("No se encontró pg_dump en la ruta configurada: " + config.getPgDumpPath());
        }

        List<String> command = new ArrayList<>();
        command.add(executable);
        command.add("-h");
        command.add(config.getDatabaseHost());
        command.add("-p");
        command.add(String.valueOf(config.getDatabasePort()));
        command.add("-U");
        command.add(config.getDatabaseUser());
        command.add("-F");
        command.add("c");
        command.add("-w");
        command.add("-f");
        command.add(outputFile.toString());
        command.add(config.getDatabaseName());

        return command;
    }

    private String readLogFile(Path logFilePath) {
        try {
            if (logFilePath != null && Files.exists(logFilePath)) {
                return Files.readString(logFilePath, StandardCharsets.UTF_8);
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    private int failExecution(BackupExecution execution, Path outputFile, int exitCode, String message) {
        execution.setStatus(BackupStatus.FAILED);
        execution.setFinishedAt(LocalDateTime.now());
        execution.setExitCode(exitCode);
        execution.setMessage(truncate(message, 4000));

        if (outputFile != null) {
            execution.setFilePath(outputFile.toString());

            try {
                if (Files.exists(outputFile)) {
                    execution.setFileSizeBytes(Files.size(outputFile));
                }
            } catch (Exception ignored) {
            }
        }

        backupExecutionRepository.saveAndFlush(execution);
        return exitCode == 0 ? 1 : exitCode;
    }

    private void validateRequest(BackupConfigRequestDTO request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("El userId es obligatorio.");
        }
        if (!StringUtils.hasText(request.getLocalPath())) {
            throw new IllegalArgumentException("La ruta local es obligatoria.");
        }
        if (!StringUtils.hasText(request.getDatabaseHost())) {
            throw new IllegalArgumentException("El host de la base es obligatorio.");
        }
        if (request.getDatabasePort() == null) {
            throw new IllegalArgumentException("El puerto de la base es obligatorio.");
        }
        if (!StringUtils.hasText(request.getDatabaseName())) {
            throw new IllegalArgumentException("El nombre de la base es obligatorio.");
        }
        if (!StringUtils.hasText(request.getDatabaseUser())) {
            throw new IllegalArgumentException("El usuario de la base es obligatorio.");
        }
        if (request.getRetentionDays() != null && request.getRetentionDays() < 1) {
            throw new IllegalArgumentException("retentionDays debe ser mayor o igual a 1.");
        }
    }

    private BackupConfigResponseDTO mapConfigToResponse(BackupConfig config) {
        return BackupConfigResponseDTO.builder()
            .id(config.getId())
            .createdBy(config.getCreatedBy())
            .updatedBy(config.getUpdatedBy())
            .active(config.getActive())
            .pgDumpPath(config.getPgDumpPath())
            .localPath(config.getLocalPath())
            .filePrefix(config.getFilePrefix())
            .databaseHost(config.getDatabaseHost())
            .databasePort(config.getDatabasePort())
            .databaseName(config.getDatabaseName())
            .databaseUser(config.getDatabaseUser())
            .pgpassFilePath(config.getPgpassFilePath())
            .cleanupEnabled(config.getCleanupEnabled())
            .retentionDays(config.getRetentionDays())
            .driveEnabled(config.getDriveEnabled())
            .driveAccountId(config.getDriveAccountId())
            .driveFolderId(config.getDriveFolderId())
            .createdAt(config.getCreatedAt())
            .updatedAt(config.getUpdatedAt())
            .build();
    }

    private BackupExecutionResponseDTO mapExecutionToResponse(BackupExecution execution) {
        return BackupExecutionResponseDTO.builder()
            .id(execution.getId())
            .backupConfigId(execution.getBackupConfigId())
            .idUsuario(execution.getIdUsuario())
            .status(execution.getStatus().name())
            .backupType(execution.getBackupType())
            .triggeredBy(execution.getTriggeredBy())
            .startedAt(execution.getStartedAt())
            .finishedAt(execution.getFinishedAt())
            .fileName(execution.getFileName())
            .filePath(execution.getFilePath())
            .fileSizeBytes(execution.getFileSizeBytes())
            .exitCode(execution.getExitCode())
            .message(execution.getMessage())
            .fileDeleted(execution.getFileDeleted())
            .deletedAt(execution.getDeletedAt())
            .deletionReason(execution.getDeletionReason())
            .cleanupMessage(execution.getCleanupMessage())
            .driveUploaded(execution.getDriveUploaded())
            .driveFileId(execution.getDriveFileId())
            .driveFileName(execution.getDriveFileName())
            .driveWebViewLink(execution.getDriveWebViewLink())
            .driveUploadedAt(execution.getDriveUploadedAt())
            .driveMessage(execution.getDriveMessage())
            .build();
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

    private record RetentionResult(int deletedCount) {
    }
}