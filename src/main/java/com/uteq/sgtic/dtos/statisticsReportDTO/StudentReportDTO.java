package com.uteq.sgtic.dtos.statisticsReportDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentReportDTO {
    private Long idEstudiante;
    private String nombreCompleto;
    private String tituloTema;
    private String nombreDirector;
    private String periodo;
    private String estado;
}
