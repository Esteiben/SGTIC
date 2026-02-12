package com.uteq.sgtic.dtos;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Integer idUser;
    private String identification;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean active;
}
