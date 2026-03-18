package com.uteq.sgtic.dtos.student;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EnrollmentResponseDTO {
    private Integer id;
    private Integer level;
    private String enrollmentType;
    private String periodStatus;
    private LocalDateTime enrollmentDate;
    private String message;
}