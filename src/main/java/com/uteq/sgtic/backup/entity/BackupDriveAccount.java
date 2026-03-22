package com.uteq.sgtic.backup.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "backup_drive_account")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BackupDriveAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "created_by", nullable = false) private Integer createdBy;
    @Column(name = "updated_by") private Integer updatedBy;
    @Column(name = "google_email", length = 200) private String googleEmail;
    @Column(name = "google_sub", length = 100) private String googleSub;
    @Column(name = "refresh_token", columnDefinition = "TEXT", nullable = false) private String refreshToken;
    @Column(columnDefinition = "TEXT") private String scope;
    @Column(nullable = false) private Boolean active;
    @Column(name = "created_at", nullable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;

    @PrePersist public void onCreate() {
        LocalDateTime now = LocalDateTime.now(); this.createdAt = now; this.updatedAt = now;
        if (this.active == null) this.active = true;
    }
    @PreUpdate public void onUpdate() { this.updatedAt = LocalDateTime.now(); }
}