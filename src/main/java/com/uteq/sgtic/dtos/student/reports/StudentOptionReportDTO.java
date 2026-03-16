package com.uteq.sgtic.dtos.student.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentOptionReportDTO {
    private Integer studentId;
    private String currentOptionName;
    private String currentOptionDescription;
    private LocalDate selectionDate;
    private LocalDate lastChangeDate;
    private String previousOptionName;
    private String changeReason;
}