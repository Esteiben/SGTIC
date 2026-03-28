package com.uteq.sgtic.backup.service;

import com.uteq.sgtic.backup.dto.BackupScheduleRequestDTO;
import com.uteq.sgtic.backup.dto.BackupScheduleResponseDTO;
import com.uteq.sgtic.backup.entity.BackupSchedule;
import com.uteq.sgtic.backup.enums.BackupDayOfWeek;
import com.uteq.sgtic.backup.enums.BackupFrequency;
import com.uteq.sgtic.backup.enums.BackupType;
import com.uteq.sgtic.backup.repository.BackupConfigRepository;
import com.uteq.sgtic.backup.repository.BackupScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class BackupScheduleService {

    private final BackupScheduleRepository backupScheduleRepository;
    private final BackupConfigRepository backupConfigRepository;
    private final WindowsTaskSchedulerService windowsTaskSchedulerService;

    public List<BackupScheduleResponseDTO> getAll() {
        return backupScheduleRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public BackupScheduleResponseDTO getById(Long id) {
        BackupSchedule schedule = backupScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No existe la tarea con id " + id));

        return mapToResponse(schedule);
    }

    public BackupScheduleResponseDTO create(BackupScheduleRequestDTO request) {
        validateRequest(request);

        backupConfigRepository.findById(request.getBackupConfigId())
                .orElseThrow(() -> new IllegalStateException("No existe backup_config con id " + request.getBackupConfigId()));

        BackupSchedule schedule = BackupSchedule.builder()
                .backupConfigId(request.getBackupConfigId())
                .createdBy(request.getUserId())
                .name(request.getName())
                .backupType(BackupType.valueOf(request.getBackupType().toUpperCase(Locale.ROOT)))
                .frequency(BackupFrequency.valueOf(request.getFrequency().toUpperCase(Locale.ROOT)))
                .dayOfWeek(parseDayOfWeek(request.getDayOfWeek()))
                .executionTime(LocalTime.parse(request.getExecutionTime()))
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        schedule = backupScheduleRepository.save(schedule);

        schedule.setWindowsTaskName(buildWindowsTaskName(schedule));
        schedule = backupScheduleRepository.save(schedule);

        syncSchedule(schedule);

        return mapToResponse(schedule);
    }

    public BackupScheduleResponseDTO update(Long id, BackupScheduleRequestDTO request) {
        validateRequest(request);

        BackupSchedule schedule = backupScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No existe la tarea con id " + id));

        backupConfigRepository.findById(request.getBackupConfigId())
                .orElseThrow(() -> new IllegalStateException("No existe backup_config con id " + request.getBackupConfigId()));

        schedule.setBackupConfigId(request.getBackupConfigId());
        schedule.setUpdatedBy(request.getUserId());
        schedule.setName(request.getName());
        schedule.setBackupType(BackupType.valueOf(request.getBackupType().toUpperCase(Locale.ROOT)));
        schedule.setFrequency(BackupFrequency.valueOf(request.getFrequency().toUpperCase(Locale.ROOT)));
        schedule.setDayOfWeek(parseDayOfWeek(request.getDayOfWeek()));
        schedule.setExecutionTime(LocalTime.parse(request.getExecutionTime()));
        schedule.setActive(request.getActive() != null ? request.getActive() : schedule.getActive());

        schedule = backupScheduleRepository.save(schedule);

        syncSchedule(schedule);

        return mapToResponse(schedule);
    }

    public void delete(Long id) {
        BackupSchedule schedule = backupScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No existe la tarea con id " + id));

        windowsTaskSchedulerService.deleteTaskIfExists(schedule.getWindowsTaskName());
        backupScheduleRepository.delete(schedule);
    }

    public BackupScheduleResponseDTO sync(Long id) {
        BackupSchedule schedule = backupScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No existe la tarea con id " + id));

        syncSchedule(schedule);
        return mapToResponse(schedule);
    }

    public void runNow(Long id) {
        BackupSchedule schedule = backupScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No existe la tarea con id " + id));

        if (!StringUtils.hasText(schedule.getWindowsTaskName())) {
            throw new IllegalStateException("La tarea no tiene windowsTaskName.");
        }

        windowsTaskSchedulerService.runTaskNow(schedule.getWindowsTaskName());
    }

    private void syncSchedule(BackupSchedule schedule) {
        try {
            if (Boolean.TRUE.equals(schedule.getActive())) {
                windowsTaskSchedulerService.createOrUpdateTask(schedule);
                schedule.setLastSyncMessage("Tarea de Windows creada/actualizada correctamente.");
            } else {
                windowsTaskSchedulerService.deleteTaskIfExists(schedule.getWindowsTaskName());
                schedule.setLastSyncMessage("Tarea desactivada y eliminada del Programador de tareas.");
            }

            schedule.setLastSyncedAt(LocalDateTime.now());
            backupScheduleRepository.save(schedule);

        } catch (Exception e) {
            schedule.setLastSyncMessage("Error al sincronizar con Windows: " + e.getMessage());
            schedule.setLastSyncedAt(LocalDateTime.now());
            backupScheduleRepository.save(schedule);
            throw e;
        }
    }

    private void validateRequest(BackupScheduleRequestDTO request) {
        if (request.getBackupConfigId() == null) {
            throw new IllegalArgumentException("backupConfigId es obligatorio.");
        }
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("userId es obligatorio.");
        }
        if (!StringUtils.hasText(request.getName())) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (!StringUtils.hasText(request.getBackupType())) {
            throw new IllegalArgumentException("El tipo de respaldo es obligatorio.");
        }
        if (!StringUtils.hasText(request.getFrequency())) {
            throw new IllegalArgumentException("La frecuencia es obligatoria.");
        }
        if (!StringUtils.hasText(request.getExecutionTime())) {
            throw new IllegalArgumentException("La hora de ejecución es obligatoria.");
        }

        BackupFrequency frequency = BackupFrequency.valueOf(request.getFrequency().toUpperCase(Locale.ROOT));
        if (frequency == BackupFrequency.WEEKLY && !StringUtils.hasText(request.getDayOfWeek())) {
            throw new IllegalArgumentException("Las tareas semanales requieren dayOfWeek.");
        }
    }

    private BackupDayOfWeek parseDayOfWeek(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return BackupDayOfWeek.valueOf(value.toUpperCase(Locale.ROOT));
    }

    private String buildWindowsTaskName(BackupSchedule schedule) {
        String cleanName = schedule.getName()
                .trim()
                .replaceAll("[^a-zA-Z0-9_-]", "_");

        return "SGTIC_Backup_" + schedule.getId() + "_" + cleanName;
    }

    private BackupScheduleResponseDTO mapToResponse(BackupSchedule schedule) {
        return BackupScheduleResponseDTO.builder()
                .id(schedule.getId())
                .backupConfigId(schedule.getBackupConfigId())
                .createdBy(schedule.getCreatedBy())
                .updatedBy(schedule.getUpdatedBy())
                .name(schedule.getName())
                .backupType(schedule.getBackupType().name())
                .frequency(schedule.getFrequency().name())
                .dayOfWeek(schedule.getDayOfWeek() != null ? schedule.getDayOfWeek().name() : null)
                .executionTime(schedule.getExecutionTime())
                .active(schedule.getActive())
                .windowsTaskName(schedule.getWindowsTaskName())
                .lastSyncMessage(schedule.getLastSyncMessage())
                .lastSyncedAt(schedule.getLastSyncedAt())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }
}