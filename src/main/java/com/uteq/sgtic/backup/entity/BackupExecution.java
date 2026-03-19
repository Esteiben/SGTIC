package com.uteq.sgtic.backup.entity;
import com.uteq.sgtic.backup.enums.BackupStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "backup_execution")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BackupExecution {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "backup_config_id", nullable = false) private Long backupConfigId;
    @Column(name = "id_usuario") private Integer idUsuario;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30) private BackupStatus status;
    @Column(name = "backup_type", nullable = false, length = 30) private String backupType;
    @Column(name = "triggered_by", nullable = false, length = 30) private String triggeredBy;
    @Column(name = "started_at", nullable = false) private LocalDateTime startedAt;
    @Column(name = "finished_at") private LocalDateTime finishedAt;
    @Column(name = "file_name", length = 300) private String fileName;
    @Column(name = "file_path", length = 700) private String filePath;
    @Column(name = "file_size_bytes") private Long fileSizeBytes;
    @Column(name = "exit_code") private Integer exitCode;
    @Column(columnDefinition = "TEXT") private String message;
    @Column(name = "file_deleted", nullable = false) private Boolean fileDeleted;
    @Column(name = "deleted_at") private LocalDateTime deletedAt;
    @Column(name = "deletion_reason", length = 100) private String deletionReason;
    @Column(name = "cleanup_message", columnDefinition = "TEXT") private String cleanupMessage;
    @Column(name = "drive_uploaded", nullable = false) private Boolean driveUploaded;
    @Column(name = "drive_file_id", length = 200) private String driveFileId;
    @Column(name = "drive_file_name", length = 300) private String driveFileName;
    @Column(name = "drive_web_view_link", columnDefinition = "TEXT") private String driveWebViewLink;
    @Column(name = "drive_uploaded_at") private LocalDateTime driveUploadedAt;
    @Column(name = "drive_message", columnDefinition = "TEXT") private String driveMessage;

    @PrePersist public void onCreate() {
        if (this.fileDeleted == null) this.fileDeleted = false;
        if (this.driveUploaded == null) this.driveUploaded = false;
    }
}