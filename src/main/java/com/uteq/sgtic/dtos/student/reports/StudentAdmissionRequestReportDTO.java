package com.uteq.sgtic.dtos.student.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAdmissionRequestReportDTO {
    private Integer admissionRequestId;
    private String identification;
    private String firstNames;
    private String lastNames;
    private String email;
    private String facultyName;
    private String careerName;
    private String academicPeriodName;
    private LocalDate submissionDate;
    private String status;
    private String observations;
    private LocalDate responseDate;
}