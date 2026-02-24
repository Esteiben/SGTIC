package com.uteq.sgtic.services;

import com.uteq.sgtic.dtos.CreateUserRequestDTO;
import com.uteq.sgtic.dtos.RoleDTO;
import com.uteq.sgtic.dtos.UserResponseDTO;

import java.util.List;

public interface IUserManagementService {

    UserCreationResult createUser(CreateUserRequestDTO request);

    List<UserResponseDTO> getAllUsers();

    List<RoleDTO> getAvailableRoles();

    record UserCreationResult(boolean success, Integer userId, String message) {}
}
