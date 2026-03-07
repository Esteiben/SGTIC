package com.uteq.sgtic.dtos.optionDegreeCareer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionDegreerCareerDTO {
    private Integer idOption;
    private String name;
    private String description;
    private String iconName;
    private Boolean seleccionado;
}
