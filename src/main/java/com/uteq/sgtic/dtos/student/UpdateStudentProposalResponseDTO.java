package com.uteq.sgtic.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentProposalResponseDTO {
    private String mensaje;
    private Integer idPropuesta;
    private Integer numeroVersion;
}