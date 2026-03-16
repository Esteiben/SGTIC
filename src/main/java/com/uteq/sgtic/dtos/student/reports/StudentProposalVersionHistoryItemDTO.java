package com.uteq.sgtic.dtos.student.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProposalVersionHistoryItemDTO {
    private Integer historyId;
    private Integer proposalId;
    private Integer topicProposedId;
    private Integer versionNumber;
    private String title;
    private String description;
    private String documentUrl;
    private String titulationOption;
    private String proposalStatus;
    private String topicStatus;
    private String teacherFeedback;
    private LocalDate submissionDate;
    private LocalDateTime changeDate;
    private String changeReason;
    private String academicPeriodName;
}