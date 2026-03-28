package com.uteq.sgtic.backup.service.strategy;
import com.uteq.sgtic.backup.entity.BackupConfig;
import com.uteq.sgtic.backup.enums.BackupType;
import org.springframework.stereotype.Service;

@Service
public class DifferentialBackupStrategyImpl implements IBackupStrategy {
    @Override public boolean supports(BackupType type) { return type == BackupType.DIFFERENTIAL; }
    @Override public void executeBackup(BackupConfig config, String triggeredBy, Integer idUsuario) { throw new UnsupportedOperationException("En desarrollo por el equipo."); }
    @Override public void restoreBackup(BackupConfig config, Long executionId, Integer idUsuarioAdmin) { throw new UnsupportedOperationException("En desarrollo."); }
}