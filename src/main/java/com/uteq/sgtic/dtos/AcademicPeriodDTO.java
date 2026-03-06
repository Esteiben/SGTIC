package com.uteq.sgtic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicPeriodDTO {
    private Integer idPeriod;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}