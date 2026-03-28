package com.uteq.sgtic.dtos.institutionalstructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareerDisplayDTO {
    private Integer id;
    private String name;
    private Boolean active;
}