package com.uteq.sgtic.backup.service;

import com.uteq.sgtic.backup.dto.*;
import com.uteq.sgtic.backup.entity.BackupConfig;
import com.uteq.sgtic.backup.entity.BackupExecution;
import com.uteq.sgtic.backup.enums.BackupStatus;
import com.uteq.sgtic.backup.enums.BackupType;
import com.uteq.sgtic.backup.repository.BackupConfigRepository;
import com.uteq.sgtic.backup.repository.BackupExecutionRepository;
import com.uteq.sgtic.backup.service.strategy.IBackupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackupManagerService {

    private final BackupConfigRepository backupConfigRepository;
    private final BackupExecutionRepository backupExecutionRepository;
    private final List<IBackupStrategy> strategies;

    // === SOLUCIÓN AL DEADLOCK: Auto-inyección (Bypass del Proxy) ===
    @Autowired
    @Lazy
    private BackupManagerService self;

    // --- MÉTODOS DE CONFIGURACIÓN ---
    public BackupConfigResponseDTO getActiveConfig() {
        BackupConfig config = backupConfigRepository.findFirstByActiveTrue()
                .orElseThrow(() -> new IllegalStateException("No existe configuración activa."));
        return mapConfigToResponse(config);
    }

    public BackupConfigResponseDTO createConfig(BackupConfigRequestDTO request) {
        BackupConfig config = BackupConfig.builder()
                .createdBy(request.getUserId())
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
        return mapConfigToResponse(backupConfigRepository.save(config));
    }

    public BackupConfigResponseDTO updateConfig(Long id, BackupConfigRequestDTO request) {
        BackupConfig config = backupConfigRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No se encontró configuración."));
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
        config.setCleanupEnabled(
                request.getCleanupEnabled() != null ? request.getCleanupEnabled() : config.getCleanupEnabled());
        config.setRetentionDays(
                request.getRetentionDays() != null ? request.getRetentionDays() : config.getRetentionDays());
        config.setDriveEnabled(
                request.getDriveEnabled() != null ? request.getDriveEnabled() : config.getDriveEnabled());
        config.setDriveAccountId(request.getDriveAccountId());
        config.setDriveFolderId(request.getDriveFolderId());
        return mapConfigToResponse(backupConfigRepository.save(config));
    }

    public List<BackupExecutionResponseDTO> getExecutionHistory() {
        return backupExecutionRepository.findAllByOrderByStartedAtDesc().stream().map(this::mapExecutionToResponse)
                .toList();
    }

    // --- MÉTODOS CORE (EL ORQUESTADOR) ---
    @Transactional
    public void runBackup(BackupType type, String triggeredBy, Integer idUsuario) {
        BackupConfig config = backupConfigRepository.findFirstByActiveTrue()
                .orElseThrow(() -> new IllegalStateException("No hay config activa."));
        IBackupStrategy strategy = strategies.stream().filter(s -> s.supports(type)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Estrategia no implementada."));

        strategy.executeBackup(config, triggeredBy, idUsuario);
        applyRetentionPolicy(config);
    }

    @Transactional
    public void restoreBackup(Long executionId, Integer idUsuarioAdmin) {
        BackupConfig config = backupConfigRepository.findFirstByActiveTrue()
                .orElseThrow(() -> new IllegalStateException("No hay config activa."));

        BackupExecution execution = backupExecutionRepository.findById(executionId).orElseThrow();
        BackupType type = BackupType.valueOf(execution.getBackupType());

        IBackupStrategy strategy = strategies.stream().filter(s -> s.supports(type)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No hay estrategia de restore para este tipo."));
        strategy.restoreBackup(config, executionId, idUsuarioAdmin);
    }

    public void recoverInterruptedExecutions() {
        List<BackupExecution> runningExecutions = backupExecutionRepository
                .findAllByStatusOrderByStartedAtDesc(BackupStatus.RUNNING);
        for (BackupExecution execution : runningExecutions) {
            execution.setStatus(BackupStatus.FAILED);
            execution.setFinishedAt(LocalDateTime.now());
            execution.setExitCode(-2);
            execution.setMessage("Proceso interrumpido inesperadamente.");
        }
        backupExecutionRepository.saveAllAndFlush(runningExecutions);
    }

    // --- LIMPIEZA Y MAPPERS ---
    private void applyRetentionPolicy(BackupConfig config) {
        if (config.getCleanupEnabled() == null || !config.getCleanupEnabled())
            return;
        int retentionDays = config.getRetentionDays() != null ? config.getRetentionDays() : 30;
        LocalDateTime cutoff = LocalDateTime.now().minusDays(retentionDays);
        List<BackupExecution> candidates = backupExecutionRepository.findRetentionCandidates(cutoff);

        for (BackupExecution candidate : candidates) {
            Path backupPath = Paths.get(candidate.getFilePath());
            Path logPath = Paths.get(candidate.getFilePath().replace(".zip", ".log"));
            try {
                Files.deleteIfExists(backupPath);
                Files.deleteIfExists(logPath);
                candidate.setFileDeleted(true);
                candidate.setDeletedAt(LocalDateTime.now());
                candidate.setDeletionReason("RETENTION_POLICY");
                backupExecutionRepository.saveAndFlush(candidate);
            } catch (Exception ignored) {
            }
        }
    }

    private BackupConfigResponseDTO mapConfigToResponse(BackupConfig config) {
        return BackupConfigResponseDTO.builder().id(config.getId()).active(config.getActive())
                .pgDumpPath(config.getPgDumpPath()).localPath(config.getLocalPath()).filePrefix(config.getFilePrefix())
                .databaseHost(config.getDatabaseHost()).databasePort(config.getDatabasePort())
                .databaseName(config.getDatabaseName()).databaseUser(config.getDatabaseUser())
                .pgpassFilePath(config.getPgpassFilePath()).cleanupEnabled(config.getCleanupEnabled())
                .retentionDays(config.getRetentionDays()).driveEnabled(config.getDriveEnabled())
                .driveAccountId(config.getDriveAccountId()).driveFolderId(config.getDriveFolderId()).build();
    }

    private BackupExecutionResponseDTO mapExecutionToResponse(BackupExecution e) {
        return BackupExecutionResponseDTO.builder()
                .id(e.getId())
                .backupConfigId(e.getBackupConfigId())
                .idUsuario(e.getIdUsuario())
                .status(e.getStatus().name())
                .backupType(e.getBackupType())
                .triggeredBy(e.getTriggeredBy())
                .startedAt(e.getStartedAt())
                .finishedAt(e.getFinishedAt())
                .fileName(e.getFileName())
                .filePath(e.getFilePath())
                .fileSizeBytes(e.getFileSizeBytes())
                .exitCode(e.getExitCode())
                .message(e.getMessage())
                // === CAMPOS DE LIMPIEZA ===
                .fileDeleted(e.getFileDeleted())
                .deletedAt(e.getDeletedAt())
                .deletionReason(e.getDeletionReason())
                .cleanupMessage(e.getCleanupMessage())
                // === CAMPOS DE DRIVE QUE FALTABAN ===
                .driveUploaded(e.getDriveUploaded())
                .driveFileId(e.getDriveFileId())
                .driveFileName(e.getDriveFileName())
                .driveWebViewLink(e.getDriveWebViewLink())
                .driveUploadedAt(e.getDriveUploadedAt())
                .driveMessage(e.getDriveMessage())
                .build();
    }

    // --- NUEVO: SINCRONIZACIÓN DE UN CLIC ---
    // NOTA: NO usamos @Transactional aquí. Usamos "self" para abrir sub-transacciones.
    public void syncToSecondary(Integer idUsuarioAdmin) {
        log.info("Iniciando proceso de sincronización a base de datos secundaria...");
        
        // 1. Disparamos el Backup usando 'self' para aislar la transacción
        self.runBackup(BackupType.FULL, "SYNC_MANUAL", idUsuarioAdmin);
        
        // 2. Buscar el ID de ese backup exacto que acabamos de crear
        BackupExecution latestSyncBackup = backupExecutionRepository.findAllByOrderByStartedAtDesc()
                .stream()
                .filter(e -> "SYNC_MANUAL".equals(e.getTriggeredBy()) && BackupStatus.SUCCESS.equals(e.getStatus()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Falló la creación del backup local. No se puede sincronizar."));
                
        // 3. Inyectar ese backup en la base secundaria usando 'self'
        self.restoreBackup(latestSyncBackup.getId(), idUsuarioAdmin);
        
        log.info("Sincronización a base secundaria completada con éxito.");
    }
}