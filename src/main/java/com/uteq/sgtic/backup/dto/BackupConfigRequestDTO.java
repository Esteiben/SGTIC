package com.uteq.sgtic.backup.dto;

//DTO para crear o actualizar la configuración del respaldo.

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackupConfigRequestDTO {

    private Integer userId;

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

    private Boolean driveEnabled;
    private Long driveAccountId;
    private String driveFolderId;
}