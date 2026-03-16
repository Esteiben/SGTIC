package com.uteq.sgtic.dtos.student.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentThesisProgressReportDTO {
    private Integer thesisWorkId;
    private Integer proposalId;
    private String thesisStatus;
    private String academicPeriodName;
    private String topicTitle;
    private LocalDate proposalSubmissionDate;
    private String proposalStatus;
    private Integer directorId;
    private String directorFullName;
    private String directorEmail;
}