package com.uteq.sgtic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private Integer id;
    private String identification;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private Boolean active;
    private List<String> roles;
}
