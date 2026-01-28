package com.uteq.sgtic.service;

import com.uteq.sgtic.dtos.LoginRequestDTO;
import com.uteq.sgtic.dtos.LoginResponseDTO;

public interface IAuthService {
    LoginResponseDTO login(LoginRequestDTO request);
}
