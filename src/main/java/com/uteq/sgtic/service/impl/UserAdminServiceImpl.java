package com.uteq.sgtic.service.impl;

import com.uteq.sgtic.dtos.UserCreateDTO;
import com.uteq.sgtic.entities.User;
import com.uteq.sgtic.repository.IUserRepository;
import com.uteq.sgtic.service.IUserAdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAdminServiceImpl implements IUserAdminService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserCreateDTO dto) {

        userRepository.createUser(
                dto.getIdentification(),
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail()
        );
    }

    @Override
    public void updateUserStatus(Integer userId, Boolean active) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(active);
        userRepository.save(user);
        // Trigger sincroniza usuario BD
    }
}
