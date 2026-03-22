package com.uteq.sgtic.backup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackupExecutionResponseDTO {

    private Long id;
    private Long backupConfigId;
    private Integer idUsuario;

    private String status;
    private String backupType;
    private String triggeredBy;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    private String fileName;
    private String filePath;
    private Long fileSizeBytes;
    private Integer exitCode;
    private String message;

    private Boolean fileDeleted;
    private LocalDateTime deletedAt;
    private String deletionReason;
    private String cleanupMessage;

    private Boolean driveUploaded;
    private String driveFileId;
    private String driveFileName;
    private String driveWebViewLink;
    private LocalDateTime driveUploadedAt;
    private String driveMessage;
}