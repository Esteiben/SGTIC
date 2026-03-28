package com.uteq.sgtic.backup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO para crear o actualizar una tarea programada.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackupScheduleRequestDTO {

    private Long backupConfigId;
    private Integer userId;

    private String name;
    private String backupType;     // FULL, DIFFERENTIAL, INCREMENTAL
    private String frequency;      // DAILY, WEEKLY
    private String dayOfWeek;      // MONDAY ... SUNDAY
    private String executionTime;  // HH:mm
    private Boolean active;
}