package com.uteq.sgtic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String email;
    private String fullName;
    private List<String> roles;
    private UserContext context;

    private Boolean primerIngreso;

    @Data
    @AllArgsConstructor
    public static class UserContext {
        private Integer idFaculty;
        private Integer idCareer;
        private Integer idTeacher;
        private Integer idStudent;
    }
}
