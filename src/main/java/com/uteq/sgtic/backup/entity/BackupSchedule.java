package com.uteq.sgtic.backup.entity;

import com.uteq.sgtic.backup.enums.BackupDayOfWeek;
import com.uteq.sgtic.backup.enums.BackupFrequency;
import com.uteq.sgtic.backup.enums.BackupType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entidad que representa una tarea programada de respaldo.
 */
@Entity
@Table(name = "backup_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackupSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "backup_config_id", nullable = false)
    private Long backupConfigId;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "backup_type", nullable = false, length = 30)
    private BackupType backupType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BackupFrequency frequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", length = 20)
    private BackupDayOfWeek dayOfWeek;

    @Column(name = "execution_time", nullable = false)
    private LocalTime executionTime;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "windows_task_name", length = 200, unique = true)
    private String windowsTaskName;

    @Column(name = "last_sync_message", columnDefinition = "TEXT")
    private String lastSyncMessage;

    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.active == null) {
            this.active = true;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}