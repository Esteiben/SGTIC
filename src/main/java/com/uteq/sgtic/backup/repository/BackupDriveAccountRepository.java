package com.uteq.sgtic.backup.repository;
import com.uteq.sgtic.backup.entity.BackupDriveAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface BackupDriveAccountRepository extends JpaRepository<BackupDriveAccount, Long> {
    Optional<BackupDriveAccount> findFirstByActiveTrue();
    Optional<BackupDriveAccount> findByIdAndActiveTrue(Long id);
}