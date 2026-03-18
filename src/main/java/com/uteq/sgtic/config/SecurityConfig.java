package com.uteq.sgtic.config;


import com.uteq.sgtic.config.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // Preflight CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Públicos
                .requestMatchers("/error").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/request-access/**").permitAll()
                    .requestMatchers("/chat-socket/**").permitAll()
                .requestMatchers("/api/public/selection/**").permitAll()
                .requestMatchers("/api/solicitudes/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/solicitudes/aprobar/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/solicitudes/rechazar/**").permitAll()

                // OAuth Google Drive público
                .requestMatchers("/api/admin/drive/oauth/**").permitAll()

                // Rutas protegidas por rol
                .requestMatchers(HttpMethod.GET, "/api/admin/catalog/periods/active")
                    .hasAuthority("administrador_sgtic")
                .requestMatchers("/api/admin/**")
                    .hasAuthority("administrador_sgtic")
                .requestMatchers("/api/coordinator/faculty/**")
                    .hasAnyAuthority("administrador_sgtic", "coordinador_facultad")
                .requestMatchers("/api/coordinator/career/**")
                    .hasAnyAuthority("administrador_sgtic", "coordinador_facultad", "coordinador_carrera")
                .requestMatchers("/api/teacher/**")
                    .hasAnyAuthority("administrador_sgtic", "docente", "director_trabajo_titulacion")
                .requestMatchers("/api/student/**")
                    .hasAuthority("estudiante")

                // Todo lo demás autenticado
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 👇 IMPORTANTE: Usar allowedOriginPatterns en lugar de allowedOrigins
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:4200",
                "https://localhost:4200"
        ));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));

        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "Cache-Control",
                "Pragma"
        ));

        configuration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Authorization"
        ));

        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 👇 APLICAR A TODAS LAS RUTAS, NO SOLO /api/**
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
