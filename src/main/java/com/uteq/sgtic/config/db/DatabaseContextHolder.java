package com.uteq.sgtic.config.db;

public class DatabaseContextHolder {
    
    // Interruptor global visible instantáneamente para todos los hilos de Tomcat
    private static volatile String activeDatabase = "PRIMARY";
    
    public static void set(String databaseType) {
        activeDatabase = databaseType;
    }
    
    public static String get() {
        return activeDatabase;
    }
    
    public static void clear() {
        activeDatabase = "PRIMARY";
    }
}