package com.uteq.sgtic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DegreeOptionDTO {
    private Integer idOption;
    private String name;
    private String description;
}
