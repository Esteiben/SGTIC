package com.uteq.sgtic.dtos.student.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentTutoringItemDTO {
    private Integer tutoringId;
    private Integer thesisWorkId;
    private LocalDateTime tutoringDate;
    private String tutoringType;
    private String modality;
    private String locationOrLink;
    private Boolean attendance;
    private Boolean registered;
    private String observations;
    private String reportUrl;
    private String directorFullName;
    private String directorEmail;
}