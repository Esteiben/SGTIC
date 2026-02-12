package com.uteq.sgtic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private Integer idUser;
    private String email;
    private String fullName;
}
