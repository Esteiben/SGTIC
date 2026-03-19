package com.uteq.sgtic.backup.repository;

import com.uteq.sgtic.backup.entity.BackupExecution;
import com.uteq.sgtic.backup.enums.BackupStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BackupExecutionRepository extends JpaRepository<BackupExecution, Long> {

    List<BackupExecution> findAllByOrderByStartedAtDesc();
    
    List<BackupExecution> findAllByStatusOrderByStartedAtDesc(BackupStatus status);

    @Query("""
        SELECT e FROM BackupExecution e
        WHERE e.status = com.uteq.sgtic.backup.enums.BackupStatus.SUCCESS
          AND e.fileDeleted = false AND e.finishedAt IS NOT NULL
          AND e.finishedAt < :cutoff AND e.filePath IS NOT NULL
          AND e.backupType != 'RESTORE'
        ORDER BY e.finishedAt ASC
    """)
    List<BackupExecution> findRetentionCandidates(@Param("cutoff") LocalDateTime cutoff);

    // --- SOLUCIÓN: USAR NATIVE QUERY EN LUGAR DE @PROCEDURE ---

    // Enviamos "null" al final porque el último parámetro de este SP es un INOUT (p_execution_id)
    @Query(value = "CALL public.sp_iniciar_ejecucion_backup(:configId, :idUsuario, :tipo, :triggeredBy, :fileName, :filePath, null)", nativeQuery = true)
    Long iniciarEjecucion(
            @Param("configId") Long configId,
            @Param("idUsuario") Integer idUsuario,
            @Param("tipo") String tipo,
            @Param("triggeredBy") String triggeredBy,
            @Param("fileName") String fileName,
            @Param("filePath") String filePath
    );

    @Modifying
    @Query(value = "CALL public.sp_finalizar_ejecucion_backup(:executionId, :status, :exitCode, :sizeBytes, :message)", nativeQuery = true)
    void finalizarEjecucion(
            @Param("executionId") Long executionId,
            @Param("status") String status,
            @Param("exitCode") Integer exitCode,
            @Param("sizeBytes") Long sizeBytes,
            @Param("message") String message
    );

    @Modifying
    @Query(value = "CALL public.sp_registrar_restauracion(:origenId, :adminId, :message)", nativeQuery = true)
    void registrarRestauracion(
            @Param("origenId") Long origenId,
            @Param("adminId") Integer adminId,
            @Param("message") String message
    );
}