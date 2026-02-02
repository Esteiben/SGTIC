package com.uteq.sgtic.dtos;

import lombok.Data;

@Data
public class CareerCreateDTO {
    private Integer idFaculty;
    private String name;
    private Boolean active;
}
