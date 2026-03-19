package com.uteq.sgtic.backup.enums;

// Tipos de respaldos soportados por el motor (y el estado de restauración)
public enum BackupType {
    FULL,
    DIFFERENTIAL,
    INCREMENTAL,
    RESTORE
}