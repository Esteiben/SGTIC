package com.uteq.sgtic.dtos.pendingProposalDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PendingProposalDTO {
    private Integer idTemaPropuesto;
    private String nombreEstudiante;
    private String identificacionEstudiante;
    private String carreraNombre;
    private String titulo;
    private String descripcion;
    private String fechaEnvio;
    private String urlDocumento;
    private String nombreModalidad;
}
