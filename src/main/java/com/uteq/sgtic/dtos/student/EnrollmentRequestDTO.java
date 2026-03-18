package com.uteq.sgtic.dtos.student;

import lombok.Data;

@Data
public class EnrollmentRequestDTO {
    private Integer studentId;
    private Integer periodId;
    private Integer admissionRequestId; // Opcional, solo para primera matrícula
}