package com.uteq.sgtic.dtos.statisticsReportDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsReportDTO {
    private long temasAprobados;
    private long temasPendientes;
    private long temasRechazados;
    private long totalTemas;
    private long temasBanco;
    private long temasPropuesta;
}
