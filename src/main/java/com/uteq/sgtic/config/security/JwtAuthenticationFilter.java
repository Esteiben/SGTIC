package com.uteq.sgtic.config.security;

<<<<<<< HEAD
import com.uteq.sgtic.repository.UserRepository; // Importamos tu repositorio
import io.jsonwebtoken.Claims; // Importamos para extraer la fecha
=======
>>>>>>> master
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
<<<<<<< HEAD
import java.util.Date;
=======
>>>>>>> master

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
<<<<<<< HEAD
    private final UserRepository userRepository; // 🔥 INYECTAMOS EL REPO PARA CONSULTAR LA BD
=======
>>>>>>> master

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
<<<<<<< HEAD
        return path.startsWith("/chat-socket") || path.startsWith("/api/auth/");
=======
        return path.startsWith("/chat-socket");
>>>>>>> master
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String requestURI = request.getRequestURI();

<<<<<<< HEAD
        log.debug("Processing request: {} - Auth Header present: {}", requestURI, authHeader != null);
=======
        log.debug("Processing request: {} - Auth Header present: {}",
                requestURI, authHeader != null);
>>>>>>> master

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No Bearer token found, continuing filter chain");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);

            log.debug("JWT extracted for user: {}", userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
<<<<<<< HEAD

                    // =================================================================================
                    // 🔥 LÓGICA DE AUDITORÍA Y EXPULSIÓN (EL BOTÓN ROJO) 🔥
                    // =================================================================================

                    // 1. Extraemos la fecha exacta en la que se generó este token en específico
                    Date fechaCreacionToken = jwtService.extractClaim(jwt, Claims::getIssuedAt);

                    // 2. Vamos a la BD y buscamos al usuario por su email para ver si lo expulsaron
                    // Usamos findByEmail porque vi que en tu AuthController lo usaste
                    userRepository.findByEmail(userEmail).ifPresentOrElse(usuario -> {
                        Date fechaExpulsion = usuario.getFechaCierreForzado(); // Asumiendo que agregaste este getter a tu entidad

                        // 3. LA REGLA DE ORO: Si el token es más viejo que la fecha de expulsión... ¡Pa' fuera!
                        if (fechaExpulsion != null && fechaCreacionToken.before(fechaExpulsion)) {
                            log.warn("🚨 ACCESO DENEGADO: El usuario {} fue expulsado del sistema a las {}. Su token es de las {}.",
                                    userEmail, fechaExpulsion, fechaCreacionToken);

                            // Forzamos un error 401 en el response y evitamos que siga el filtro
                            try {
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Tu sesión ha sido cerrada por un administrador.");
                            } catch (IOException e) {
                                log.error("Error al enviar el código 401", e);
                            }
                        } else {
                            // Si todo está bien y no ha sido expulsado, lo dejamos pasar y armamos su sesión
                            armarSesionEnContexto(request, userDetails, userEmail);
                        }
                    }, () -> {
                        log.warn("Usuario {} no encontrado en la BD durante el chequeo de expulsión.", userEmail);
                    });

=======
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authentication set for user: {}", userEmail);
>>>>>>> master
                } else {
                    log.warn("Invalid JWT token for user: {}", userEmail);
                }
            }
        } catch (Exception e) {
            log.error("Error processing JWT: {}", e.getMessage());
<<<<<<< HEAD
            // Si el token expiró u ocurrió otro error, mandamos 401
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
            return;
        }

        // Si la respuesta ya se comprometió (mandamos el 401), no seguimos con la cadena de filtros
        if (!response.isCommitted()) {
            filterChain.doFilter(request, response);
        }
    }

    // Método auxiliar para mantener el código limpio
    private void armarSesionEnContexto(HttpServletRequest request, UserDetails userDetails, String userEmail) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.debug("Authentication set for user: {}", userEmail);
    }
}
=======
        }

        
        filterChain.doFilter(request, response);
    }
}
>>>>>>> master
