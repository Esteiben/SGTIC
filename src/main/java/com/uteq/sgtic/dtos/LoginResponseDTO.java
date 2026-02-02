package com.uteq.sgtic.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private String token;
    private Integer idUser;
    private String username;
    private String email;
}
