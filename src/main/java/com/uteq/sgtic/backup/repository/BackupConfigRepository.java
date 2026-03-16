package com.uteq.sgtic.backup.repository;

import com.uteq.sgtic.backup.entity.BackupConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para manejar la configuración de backups.
 */
public interface BackupConfigRepository extends JpaRepository<BackupConfig, Long> {

    /**
     * Obtiene la primera configuración activa.
     * En esta fase asumimos una sola configuración activa.
     */
    Optional<BackupConfig> findFirstByActiveTrue();
}