package com.uteq.sgtic.backup.dto;

//DTO de respuesta para mostrar la configuración activa.

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackupConfigResponseDTO {

    private Long id;
    private Integer createdBy;
    private Integer updatedBy;

    private Boolean active;
    private String pgDumpPath;
    private String localPath;
    private String filePrefix;
    private String databaseHost;
    private Integer databasePort;
    private String databaseName;
    private String databaseUser;
    private String pgpassFilePath;

    private Boolean cleanupEnabled;
    private Integer retentionDays;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Boolean driveEnabled;
    private Long driveAccountId;
    private String driveFolderId;
}