package com.uteq.sgtic.dtos;

import lombok.Data;
import java.util.List;

@Data
public class CreateUserRequestDTO {
    private String identification;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private List<Integer> roleIds;
    private Integer idCareer;
    private Integer idPeriod;
}