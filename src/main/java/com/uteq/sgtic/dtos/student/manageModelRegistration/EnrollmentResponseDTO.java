package com.uteq.sgtic.dtos.student.manageModelRegistration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponseDTO {
    private Boolean exito;
    private Integer idMatricula;
    private Integer nivelMatriculado;
    private String mensaje;
}