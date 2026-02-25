package com.uteq.sgtic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareerDTO {
    private Integer idCareer;
    private String name;
    private String facultyName;
}