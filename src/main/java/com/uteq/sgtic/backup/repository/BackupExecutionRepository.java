package com.uteq.sgtic.backup.repository;

import com.uteq.sgtic.backup.entity.BackupExecution;
import com.uteq.sgtic.backup.enums.BackupStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BackupExecutionRepository extends JpaRepository<BackupExecution, Long> {

    List<BackupExecution> findAllByOrderByStartedAtDesc();

    List<BackupExecution> findAllByStatusOrderByStartedAtDesc(BackupStatus status);

    @Query("""
        SELECT e
        FROM BackupExecution e
        WHERE e.status = com.uteq.sgtic.backup.enums.BackupStatus.SUCCESS
          AND e.fileDeleted = false
          AND e.finishedAt IS NOT NULL
          AND e.finishedAt < :cutoff
          AND e.filePath IS NOT NULL
        ORDER BY e.finishedAt ASC
    """)
    List<BackupExecution> findRetentionCandidates(@Param("cutoff") LocalDateTime cutoff);
}