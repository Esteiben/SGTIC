package com.uteq.sgtic.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterProposalStudentTopicResponseDTO {

    private Integer idPropuesta;
    private String mensaje;
    private String urlDocumento;
}