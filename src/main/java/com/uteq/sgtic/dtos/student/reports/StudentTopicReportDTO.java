package com.uteq.sgtic.dtos.student.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentTopicReportDTO {
    private Integer proposalId;
    private String topicSource;
    private String topicTitle;
    private String topicDescription;
    private String titulationOption;
    private String academicPeriodName;
    private LocalDate proposalSubmissionDate;
    private String proposalStatus;
    private String proposalObservations;
    private String commissionName;
    private String topicStatus;
    private String documentUrl;
    private String teacherFeedback;
    private Integer versionNumber;
}