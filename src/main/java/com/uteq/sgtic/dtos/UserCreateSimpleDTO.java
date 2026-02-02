package com.uteq.sgtic.dtos;

import lombok.Data;

@Data
public class UserCreateSimpleDTO {
    private String identification;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean active;
}
