package com.uteq.sgtic.backup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

//DTO de respuesta para una tarea programada.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackupScheduleResponseDTO {

    private Long id;
    private Long backupConfigId;
    private Integer createdBy;
    private Integer updatedBy;

    private String name;
    private String backupType;
    private String frequency;
    private String dayOfWeek;
    private LocalTime executionTime;

    private Boolean active;
    private String windowsTaskName;
    private String lastSyncMessage;
    private LocalDateTime lastSyncedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}