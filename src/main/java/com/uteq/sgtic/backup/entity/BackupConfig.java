package com.uteq.sgtic.backup.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "backup_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackupConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "pg_dump_path", length = 500)
    private String pgDumpPath;

    @Column(name = "local_path", nullable = false, length = 500)
    private String localPath;

    @Column(name = "file_prefix", nullable = false, length = 100)
    private String filePrefix;

    @Column(name = "database_host", nullable = false, length = 200)
    private String databaseHost;

    @Column(name = "database_port", nullable = false)
    private Integer databasePort;

    @Column(name = "database_name", nullable = false, length = 200)
    private String databaseName;

    @Column(name = "database_user", nullable = false, length = 200)
    private String databaseUser;

    @Column(name = "pgpass_file_path", length = 500)
    private String pgpassFilePath;

    @Column(name = "cleanup_enabled", nullable = false)
    private Boolean cleanupEnabled;

    @Column(name = "retention_days", nullable = false)
    private Integer retentionDays;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "drive_enabled", nullable = false)
    private Boolean driveEnabled;

    @Column(name = "drive_account_id")
    private Long driveAccountId;

    @Column(name = "drive_folder_id", length = 200)
    private String driveFolderId;

    // Esto es un disparador para que envíe los datos como decir estaticos en caso de que uno este null
    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.active == null) {
            this.active = true;
        }
        if (this.databasePort == null) {
            this.databasePort = 5432;
        }
        if (this.filePrefix == null || this.filePrefix.isBlank()) {
            this.filePrefix = "sgtic";
        }
        if (this.cleanupEnabled == null) {
            this.cleanupEnabled = true;
        }
        if (this.retentionDays == null || this.retentionDays < 1) {
            this.retentionDays = 30;
        }

        if (this.driveEnabled == null) {
            this.driveEnabled = false;
        }
    }

    //Esto es las autorias al hacer un update
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}