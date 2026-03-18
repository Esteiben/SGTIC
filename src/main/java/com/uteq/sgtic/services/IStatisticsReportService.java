package com.uteq.sgtic.services;


import com.uteq.sgtic.dtos.AcademicPeriodDTO;
import com.uteq.sgtic.dtos.statisticsReportDTO.StatisticsReportDTO;
import com.uteq.sgtic.dtos.statisticsReportDTO.StudentReportDTO;
import com.uteq.sgtic.dtos.statisticsReportDTO.TeacherReportDTO;

import java.util.List;

public interface IStatisticsReportService {
    StatisticsReportDTO getEstadisticas(Integer idCarrera, Integer idPeriodo);
    List<TeacherReportDTO> getDocentesReporte(Integer idCarrera, Integer idPeriodo);
    List<StudentReportDTO> getEstudiantesReporte(Integer idCarrera, Integer idPeriodo, String estado, Integer idDirector);
    List<AcademicPeriodDTO> getPeriodos();
    String getNombreCarrera(Integer idCarrera);
    List<String> getEstadosTesis();
}

