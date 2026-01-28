package com.uteq.sgtic.service;

import com.uteq.sgtic.dtos.UserCreateDTO;

public interface IUserAdminService {
    void createUser(UserCreateDTO dto);
    void updateUserStatus(Integer userId, Boolean active);
}
