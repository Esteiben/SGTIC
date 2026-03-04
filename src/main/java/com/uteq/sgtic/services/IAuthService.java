package com.uteq.sgtic.services;

import com.uteq.sgtic.dtos.LoginRequestDTO;
import com.uteq.sgtic.dtos.LoginResponseDTO;

public interface IAuthService {
    LoginResponseDTO authenticate(LoginRequestDTO request);
    boolean changeFirstPassword(Integer userId, String newPassword);
}
