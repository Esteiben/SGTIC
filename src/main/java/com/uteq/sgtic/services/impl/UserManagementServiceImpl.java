package com.uteq.sgtic.services.impl;

import com.uteq.sgtic.dtos.CreateUserRequestDTO;
import com.uteq.sgtic.dtos.UserResponseDTO;
import com.uteq.sgtic.dtos.RoleDTO;
import com.uteq.sgtic.repository.UserManagementRepository;
import com.uteq.sgtic.services.IUserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements IUserManagementService {
    private final UserManagementRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserCreationResult createUser(CreateUserRequestDTO request) {
        try {
            // Validaciones Básicas
            if (!StringUtils.hasText(request.getIdentification())) {
                return new UserCreationResult(false, null, "La identificación es obligatoria");
            }
            if (!StringUtils.hasText(request.getFirstName())) {
                return new UserCreationResult(false, null, "Los nombres son obligatorios");
            }
            if (!StringUtils.hasText(request.getLastName())) {
                return new UserCreationResult(false, null, "Los apellidos son obligatorios");
            }
            if (!StringUtils.hasText(request.getEmail()) || !request.getEmail().contains("@")) {
                return new UserCreationResult(false, null, "El correo es inválido");
            }
            if (!StringUtils.hasText(request.getUsername())) {
                return new UserCreationResult(false, null, "El nombre de usuario es obligatorio");
            }
            if (!StringUtils.hasText(request.getPassword()) || request.getPassword().length() < 6) {
                return new UserCreationResult(false, null, "La contraseña debe tener al menos 6 caracteres");
            }
            if (request.getRoleIds() == null || request.getRoleIds().isEmpty()) {
                return new UserCreationResult(false, null, "Debe asignar al menos un rol");
            }

            List<RoleDTO> availableRoles = getAvailableRoles();

            boolean hasStudentRole = request.getRoleIds().stream().anyMatch(roleId ->
                    availableRoles.stream().anyMatch(r -> r.getId().equals(roleId) && "estudiante".equalsIgnoreCase(r.getName()))
            );

            if (hasStudentRole && (request.getIdCareer() == null || request.getIdPeriod() == null)) {
                return new UserCreationResult(false, null,
                        "Para crear un estudiante debe especificar la carrera y el período académico");
            }

            String hashedPassword = passwordEncoder.encode(request.getPassword());

            Integer[] rolesArray = request.getRoleIds().toArray(new Integer[0]);

            UserManagementRepository.CreationResult result = userRepository.createUserComplete(
                    request.getIdentification(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getUsername(),
                    hashedPassword,
                    rolesArray,
                    request.getIdCareer(),
                    request.getIdPeriod(),
                    request.getIdFaculty()
            );

            // Respuesta
            if (Boolean.TRUE.equals(result.getSuccess())) {
                log.info("Usuario creado exitosamente con ID: {}", result.getUserId());
                return new UserCreationResult(true, result.getUserId(), "Usuario creado exitosamente");
            } else {
                log.warn("Error al crear usuario: {}", result.getMessage());
                return new UserCreationResult(false, null, result.getMessage());
            }

        } catch (Exception e) {
            log.error("Excepción al crear usuario: {}", e.getMessage());
            return new UserCreationResult(false, null, "Error interno: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        List<UserManagementRepository.UserProjection> projections = userRepository.getAllUsersWithRoles();

        return projections.stream()
                .map(p -> new UserResponseDTO(
                        p.getIdUsuario(),
                        p.getIdentificacion(),
                        p.getNombres(),
                        p.getApellidos(),
                        p.getCorreo(),
                        p.getUsername(),
                        p.getActivo(),
                        convertRolesToList(p.getRoles())
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getAvailableRoles() {
        return userRepository.getAvailableRoles()
                .stream()
                .map(r -> new RoleDTO(r.getIdRol(), r.getNombre()))
                .collect(Collectors.toList());
    }

    private List<String> convertRolesToList(Object rolesObject) {
        if (rolesObject == null) {
            return List.of();
        }
        if (rolesObject instanceof String[]) {
            return Arrays.asList((String[]) rolesObject);
        }
        if (rolesObject instanceof List) {
            return (List<String>) rolesObject;
        }
        return List.of(rolesObject.toString());
    }

    private String validateRoleCombination(List<Integer> roleIds, List<RoleDTO> availableRoles) {
        // Obtener nombres de roles seleccionados
        List<String> selectedRoleNames = availableRoles.stream()
                .filter(r -> roleIds.contains(r.getId()))
                .map(RoleDTO::getName)
                .collect(Collectors.toList());
        // Regla 1: Si es estudiante, no puede tener roles administrativos
        if (selectedRoleNames.contains("estudiante")) {
            List<String> invalidRoles = selectedRoleNames.stream()
                    .filter(r -> !r.equals("estudiante"))
                    .filter(r -> r.contains("admin") || r.contains("coordinador")
                            || r.contains("docente") || r.contains("director")
                            || r.contains("miembro"))
                    .collect(Collectors.toList());

            if (!invalidRoles.isEmpty()) {
                return "Un estudiante no puede tener roles administrativos o docentes";
            }
        }
        // Regla 2: Si tiene rol de comisión/tribunal, debe ser docente
        boolean hasCommissionRole = selectedRoleNames.stream()
                .anyMatch(r -> r.contains("miembro") || r.contains("comision"));
        boolean isTeacher = selectedRoleNames.contains("docente");

        if (hasCommissionRole && !isTeacher) {
            return "Los miembros de comisiones y tribunales deben ser docentes";
        }
        // Regla 3: Si es director, debe ser docente
        if (selectedRoleNames.contains("director_trabajo_titulacion") && !isTeacher) {
            return "El director de trabajo debe ser docente";
        }

        return null;
    }
}
