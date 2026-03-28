package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.CreateUserRequestDTO;
import com.uteq.sgtic.dtos.UserResponseDTO;
import com.uteq.sgtic.dtos.RoleDTO;
import com.uteq.sgtic.services.IUserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")

public class UserManagementController {

    private final IUserManagementService userService;

    @PostMapping
    @PreAuthorize("hasAuthority('administrador_sgtic')")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequestDTO request) {
        log.info("Creando usuario: {}", request.getEmail());

        IUserManagementService.UserCreationResult result = userService.createUser(request);

        if (result.success()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "success", true,
                            "message", result.message(),
                            "userId", result.userId()
                    )
            );
        } else {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", result.message()
                    )
            );
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('administrador_sgtic')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("Obteniendo lista de usuarios");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('administrador_sgtic')")
    public ResponseEntity<List<RoleDTO>> getAvailableRoles() {
        return ResponseEntity.ok(userService.getAvailableRoles());
    }
}
