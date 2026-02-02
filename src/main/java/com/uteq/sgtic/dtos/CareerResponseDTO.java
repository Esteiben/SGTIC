package com.uteq.sgtic.dtos;

import lombok.Data;

@Data
public class CareerResponseDTO {
    private Integer idCareer;
    private Integer idFaculty;
    private String name;
    private Boolean active;
}
