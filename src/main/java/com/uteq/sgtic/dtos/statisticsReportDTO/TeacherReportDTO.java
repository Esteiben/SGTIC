package com.uteq.sgtic.dtos.statisticsReportDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherReportDTO {
    private Long idDocente;
    private String nombreCompleto;
    private String especializacion;
    private long proyectosAsignados;
}
