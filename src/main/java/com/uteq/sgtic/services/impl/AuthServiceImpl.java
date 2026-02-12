package com.uteq.sgtic.services.impl;

import com.uteq.sgtic.dtos.LoginRequestDTO;
import com.uteq.sgtic.dtos.LoginResponseDTO;
import com.uteq.sgtic.entities.Credential;
import com.uteq.sgtic.entities.User;
import com.uteq.sgtic.repository.CredentialRepository;
import com.uteq.sgtic.repository.UserRepository;
import com.uteq.sgtic.services.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!Boolean.TRUE.equals(user.getActive())){
            throw new RuntimeException("User not active");
        }
        Credential creddential = credentialRepository.findByIdUser(user.getIdUser())
                .orElseThrow(() -> new RuntimeException("Credential not found"));
        if (!creddential.getPasswordHash().equals(request.getPassword())){
            throw new RuntimeException("Password not match");
        }
        return new LoginResponseDTO(
                user.getIdUser(),
                user.getEmail(),
                user.getFirstName() + ' ' + user.getLastName()
        );
    }
}
