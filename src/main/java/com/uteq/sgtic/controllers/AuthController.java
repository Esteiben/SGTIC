package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.LoginRequestDTO;
import com.uteq.sgtic.dtos.LoginResponseDTO;
import com.uteq.sgtic.dtos.NewPasswordDTO;
import com.uteq.sgtic.repository.UserRepository;
import com.uteq.sgtic.services.IAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Temporal para debug
public class AuthController {

    private final IAuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        log.info("Login attempt for email: {}", request.getEmail());
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });
        return ResponseEntity.ok(authService.authenticate(request));
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
            // 1. Usamos 'Principal' para leer el email del token de quien hace la petición
            String email = principal.getName();

            // 2. Buscamos el userId en la base de datos usando ese email
            Integer userId = userRepository.findCredentialsByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                    .getUserId();

            // 3. Llamamos al servicio mágico que creaste
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
}
