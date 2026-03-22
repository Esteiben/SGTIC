package com.uteq.sgtic.dtos.teacherAssignmentDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PendingProjectDTO {
    private Integer idPropuesta;
    private String titulo;
    private String nombreEstudiante;
    private String descripcion;
    private String fechaEnvio;
}
