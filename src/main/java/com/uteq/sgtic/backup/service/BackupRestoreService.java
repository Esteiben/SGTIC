package com.uteq.sgtic.backup.service;

import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class BackupRestoreService {

    private final String DB_PASSWORD = "p$qlAdm1n12"; 
    private final String BACKUP_DIR = "C:\\SGTIC\\backups\\";
    private final String PG_RESTORE_PATH = "C:\\Program Files\\PostgreSQL\\18\\bin\\pg_restore.exe";

    public boolean ejecutarRecuperacionTotal(String nombreArchivoZip) {
        try {
            String archivoDescomprimido = descomprimirZip(nombreArchivoZip);
            if (archivoDescomprimido == null) return false;

            if (!recrearBaseDatosPrincipal()) return false;

            boolean exitoRestauracion = ejecutarPgRestore(archivoDescomprimido);

            new File(BACKUP_DIR + archivoDescomprimido).delete();

            return exitoRestauracion;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String descomprimirZip(String zipFileName) throws IOException {
        String destFile = null;
        File destDir = new File(BACKUP_DIR);
        byte[] buffer = new byte[1024];
        
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(BACKUP_DIR + zipFileName))) {
            ZipEntry zipEntry = zis.getNextEntry();
            if (zipEntry != null) {
                // Solo nos interesa el primer archivo (el .backup)
                destFile = zipEntry.getName();
                File newFile = new File(destDir, destFile);
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
            }
            zis.closeEntry();
        }
        return destFile;
    }

    private boolean recrearBaseDatosPrincipal() {
        try {
            String url = "jdbc:postgresql://demoserver-pgsql.postgres.database.azure.com:5432/postgres?sslmode=require";
            
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection(url, "psqlAuser", DB_PASSWORD);
                 java.sql.Statement stmt = conn.createStatement()) {
                 
                conn.setAutoCommit(true); 
                
                // CAMBIO AQUÍ: Volvemos a sgtic
                stmt.execute("DROP DATABASE IF EXISTS sgtic WITH (FORCE)");
                stmt.execute("CREATE DATABASE sgtic");
                
                System.out.println("Base de datos 'sgtic' recreada con éxito en Azure.");
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error crítico recreando la BD: " + e.getMessage());
            e.printStackTrace(); 
            return false;
        }
    }

    private boolean ejecutarPgRestore(String nombreArchivoDescomprimido) {
        try {
            String archivoBackupCompleto = BACKUP_DIR + nombreArchivoDescomprimido; 

            ProcessBuilder pb = new ProcessBuilder(
                PG_RESTORE_PATH, 
                "-h", "demoserver-pgsql.postgres.database.azure.com",
                "-p", "5432",
                "-U", "psqlAuser",
                "-d", "sgtic",
                "--no-owner",      
                "--no-privileges", 
                "-1",              
                archivoBackupCompleto
            );

            pb.redirectErrorStream(true);
            pb.environment().put("PGPASSWORD", DB_PASSWORD); 

            Process process = pb.start();

            try (java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    System.out.println("[pg_restore] " + linea);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("Proceso de restauración finalizó con código: " + exitCode);
            
            return exitCode == 0;

        } catch (Exception e) {
            System.err.println("Fallo al ejecutar pg_restore: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}