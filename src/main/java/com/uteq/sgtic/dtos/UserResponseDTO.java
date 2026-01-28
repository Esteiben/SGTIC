package com.uteq.sgtic.dtos;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Integer idUser;
    private String username;
    private String email;
    private Boolean active;
}
