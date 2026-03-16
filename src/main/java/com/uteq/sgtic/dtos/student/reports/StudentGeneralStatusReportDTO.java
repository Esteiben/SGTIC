package com.uteq.sgtic.dtos.student.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentGeneralStatusReportDTO {
    private Integer studentId;
    private String studentFullName;
    private String studentEmail;
    private String careerName;
    private String academicPeriodName;
    private String titulationStatus;
    private String currentTitulationOption;
    private LocalDate optionSelectionDate;
    private String titulationPeriodType;
    private Boolean titulationPeriodApproved;
    private LocalDate approvalDate;
    private String observations;
}