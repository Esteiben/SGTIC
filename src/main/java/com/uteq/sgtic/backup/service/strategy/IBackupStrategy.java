package com.uteq.sgtic.backup.service.strategy;
import com.uteq.sgtic.backup.entity.BackupConfig;
import com.uteq.sgtic.backup.enums.BackupType;

public interface IBackupStrategy {
    boolean supports(BackupType type);
    void executeBackup(BackupConfig config, String triggeredBy, Integer idUsuario);
    void restoreBackup(BackupConfig config, Long executionId, Integer idUsuarioAdmin);
}