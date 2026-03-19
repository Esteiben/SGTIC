package com.uteq.sgtic.backup.repository;
import com.uteq.sgtic.backup.entity.BackupConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface BackupConfigRepository extends JpaRepository<BackupConfig, Long> {
    Optional<BackupConfig> findFirstByActiveTrue();
}