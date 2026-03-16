package com.uteq.sgtic.backup.repository;

import com.uteq.sgtic.backup.entity.BackupSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BackupScheduleRepository extends JpaRepository<BackupSchedule, Long> {
    List<BackupSchedule> findAllByOrderByCreatedAtDesc();
}