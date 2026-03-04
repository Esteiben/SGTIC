package com.uteq.sgtic.services.impl;

import com.uteq.sgtic.config.security.JwtService;
import com.uteq.sgtic.config.security.UserDetailsServiceImpl;
import com.uteq.sgtic.dtos.LoginRequestDTO;
import com.uteq.sgtic.dtos.LoginResponseDTO;
import com.uteq.sgtic.repository.RoleRepository;
import com.uteq.sgtic.repository.UserContextRepository;
import com.uteq.sgtic.repository.UserRepository;
import com.uteq.sgtic.services.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.uteq.sgtic.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService; // CAMBIAR: Usar interfaz, no impl
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final UserContextRepository userContextRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO authenticate(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        UserRepository.UserCredentialsProjection userData = userRepository
                .findCredentialsByEmail(request.getEmail())
                .orElseThrow();

        Integer userId = userData.getUserId();
        List<String> roles = roleRepository.findRoleNamesByUserId(userId);

        UserContextRepository.UserContextProjection context =
                userContextRepository.findUserContext(userId);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", roles);
        extraClaims.put("userId", userId);
        extraClaims.put("fullName", userData.getFirstName() + " " + userData.getLastName());

        if (context.getIdFaculty() != null) {
            extraClaims.put("idFaculty", context.getIdFaculty());
        }
        if (context.getIdCareer() != null) {
            extraClaims.put("idCareer", context.getIdCareer());
        }
        if (context.getIdTeacher() != null) {
            extraClaims.put("idTeacher", context.getIdTeacher());
        }
        if (context.getIdStudent() != null) {
            extraClaims.put("idStudent", context.getIdStudent());
        }

        String jwtToken = jwtService.generateToken(userDetails, extraClaims);

        LoginResponseDTO.UserContext responseContext = new LoginResponseDTO.UserContext(
                context.getIdFaculty(),
                context.getIdCareer(),
                context.getIdTeacher(),
                context.getIdStudent()
        );

        

        Boolean esPrimerIngreso = userRepository.findById(userId)
                .map(User::getPrimerIngreso)
                .orElse(true);

        return new LoginResponseDTO(
                jwtToken,
                userData.getEmail(),
                userData.getFirstName() + " " + userData.getLastName(),
                roles,
                responseContext,
                esPrimerIngreso
        );
    }

    public boolean changeFirstPassword(Integer userId, String newPassword) {
        // 1. Spring Boot encripta la clave con Bcrypt (costo 10 automático)
        String newHash = passwordEncoder.encode(newPassword);

        // 2. Mandamos el hash al Stored Procedure
        Boolean success = userRepository.updatePasswordSp(userId, newHash);
        
        if (success == null || !success) {
            throw new RuntimeException("Error al actualizar la contraseña en la base de datos");
        }
        
        return true;
    }
}
