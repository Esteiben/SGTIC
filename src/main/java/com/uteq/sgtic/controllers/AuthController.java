package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.LoginRequestDTO;
import com.uteq.sgtic.dtos.LoginResponseDTO;
import com.uteq.sgtic.services.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Temporal para debug
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        log.info("Login attempt for email: {}", request.getEmail());
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken() {
        return ResponseEntity.ok("Valid");
    }
}
