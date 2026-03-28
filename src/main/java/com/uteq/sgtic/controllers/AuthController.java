package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.LoginRequestDTO;
import com.uteq.sgtic.dtos.LoginResponseDTO;
import com.uteq.sgtic.dtos.NewPasswordDTO;
import com.uteq.sgtic.repository.UserRepository;
import com.uteq.sgtic.services.IAuthService;
import jakarta.servlet.http.HttpServletRequest; // IMPORTANTE: Usamos jakarta en Spring Boot 3
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final IAuthService authService;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request, HttpServletRequest httpRequest) {
        log.info("Login attempt for email: {}", request.getEmail());

        LoginResponseDTO response = authService.authenticate(request);

        String ipAddress = getClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        if (userAgent != null && userAgent.length() > 250) {
            userAgent = userAgent.substring(0, 250);
        }

        String finalUserAgent = userAgent;
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {

            // 1. aplicamos "el perdón" y actualizamos el último acceso directo con sql
            String sqlUpdate = "update public.usuario set ultimo_acceso = now(), fecha_cierre_forzado = null where id_usuario = ?";
            try {
                jdbcTemplate.update(sqlUpdate, user.getId());
                log.info("se limpió el baneo y se actualizó el acceso para el usuario id: {}", user.getId());
            } catch (Exception e) {
                log.error("error al aplicar el perdón en la bd: {}", e.getMessage());
            }

            // 2. guardamos la ip en la auditoría
            String sqlInsert = "insert into public.audit_login (id_usuario, exitoso, ip_address, user_agent) values (?, true, cast(? as inet), ?)";
            try {
                jdbcTemplate.update(sqlInsert, user.getId(), ipAddress, finalUserAgent);
                log.info("auditoría de ip guardada exitosamente para: {}", ipAddress);
            } catch (Exception e) {
                log.error("error al guardar la ip en audit_login: {}", e.getMessage());
            }
        });

        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken() {
        return ResponseEntity.ok("Valid");
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changeFirstPassword(
            Principal principal,
            @RequestBody NewPasswordDTO request) {

        try {
            String email = principal.getName();

            Integer userId = userRepository.findCredentialsByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                    .getUserId();

            boolean success = authService.changeFirstPassword(userId, request.getNewPassword());

            return ResponseEntity.ok(Map.of(
                    "success", success,
                    "message", "Contraseña actualizada correctamente"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error: " + e.getMessage()
            ));
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }
}