package com.uteq.sgtic.dtos.student.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentTopicChangeHistoryItemDTO {
    private Integer changeId;
    private String academicPeriodName;
    private String previousTopicTitle;
    private String newTopicTitle;
    private String previousOptionName;
    private String newOptionName;
    private LocalDate changeDate;
    private String reason;
}