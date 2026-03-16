package com.uteq.sgtic.dtos.student.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDirectorAssignmentReportDTO {
    private Integer proposalId;
    private Integer assignmentId;
    private LocalDate assignmentDate;
    private String response;
    private String observations;
    private Integer directorId;
    private String directorFullName;
    private String directorEmail;
    private String topicTitle;
    private String proposalStatus;
}