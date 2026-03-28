package com.uteq.sgtic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DegreeOptionDTO {
    private Integer idOption;
    private String name;
    private String description;
    private Boolean active;
    private String iconName;
    private LocalDate createdAt;
}
