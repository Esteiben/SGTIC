package com.uteq.sgtic.dtos.student.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAuthContextDTO {
    private Integer studentId;
    private Integer userId;
    private String email;
    private String fullName;
    private Integer careerId;
    private String careerName;
    private Integer academicPeriodId;
    private String academicPeriodName;
    private String titulationStatus;
}