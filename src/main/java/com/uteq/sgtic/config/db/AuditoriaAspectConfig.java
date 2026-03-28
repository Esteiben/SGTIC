package com.uteq.sgtic.config.db;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditoriaAspectConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void inyectarUsuarioEnPostgres() {
        try {
            Integer idUsuarioActual = null;

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {

                try {
                    Object principal = authentication.getPrincipal();
                    idUsuarioActual = (Integer) principal.getClass().getMethod("getIdUsuario").invoke(principal);
                } catch (Exception e) {
                    try {
                        Object principal = authentication.getPrincipal();
                        idUsuarioActual = (Integer) principal.getClass().getMethod("getId").invoke(principal);
                    } catch (Exception ex) {
                        System.out.println("Aviso: No se encontró el método getId() o getIdUsuario() en tu clase UserDetails.");
                    }
                }
            }

            if (idUsuarioActual != null) {
                jdbcTemplate.execute("SET LOCAL mi_app.id_usuario = '" + idUsuarioActual + "'");
                System.out.println("Auditoría: Usuario real ID " + idUsuarioActual + " inyectado en PostgreSQL.");
            } else {
                jdbcTemplate.execute("SELECT set_config('mi_app.id_usuario', '', true)");            }

        } catch (Exception e) {
            System.out.println("Aviso Auditoría: No se pudo inyectar el usuario - " + e.getMessage());
        }
    }
}