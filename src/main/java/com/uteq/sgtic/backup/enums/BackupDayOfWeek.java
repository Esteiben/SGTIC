package com.uteq.sgtic.backup.enums;

// Días de la semana para tareas semanales.
public enum BackupDayOfWeek {
    MONDAY("MON"),
    TUESDAY("TUE"),
    WEDNESDAY("WED"),
    THURSDAY("THU"),
    FRIDAY("FRI"),
    SATURDAY("SAT"),
    SUNDAY("SUN");

    private final String windowsCode;

    BackupDayOfWeek(String windowsCode) {
        this.windowsCode = windowsCode;
    }

    public String getWindowsCode() {
        return windowsCode;
    }
}