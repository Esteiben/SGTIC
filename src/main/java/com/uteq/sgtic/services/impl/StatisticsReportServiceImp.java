package com.uteq.sgtic.services.impl;

import com.uteq.sgtic.dtos.AcademicPeriodDTO;
import com.uteq.sgtic.dtos.statisticsReportDTO.StatisticsReportDTO;
import com.uteq.sgtic.dtos.statisticsReportDTO.StudentReportDTO;
import com.uteq.sgtic.dtos.statisticsReportDTO.TeacherReportDTO;
import com.uteq.sgtic.repository.CareerRepository;
import com.uteq.sgtic.repository.StudentRepository;
import com.uteq.sgtic.repository.TeacherRepository;
import com.uteq.sgtic.repository.AcademicPeriodRepository;
import com.uteq.sgtic.services.IStatisticsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsReportServiceImp implements IStatisticsReportService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AcademicPeriodRepository periodoRepository;

    @Autowired
    private CareerRepository careerRepository;

    @Override
    public List<AcademicPeriodDTO> getPeriodos() {
        return periodoRepository.findAll().stream()
                .map(p -> new AcademicPeriodDTO(
                        p.getIdPeriod(),
                        p.getName(),
                        p.getStartDate(),
                        p.getEndDate(),
                        p.getActive(),
                        p.getEnrollmentDeadline()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String getNombreCarrera(Integer idCarrera) {
        return careerRepository.findById(idCarrera)
                .map(c -> c.getName())
                .orElse("Carrera no encontrada");
    }

    @Override
    public List<String> getEstadosTesis() {
        return studentRepository.findDistinctEstados();
    }

    @Override
    public StatisticsReportDTO getEstadisticas(Integer idCarrera, Integer idPeriodo) {
        Integer pPeriodo = (idPeriodo != null) ? idPeriodo : 0;
        List<Object[]> result = studentRepository.callEstadisticasReporteFunction(idCarrera, pPeriodo);

        if (result != null && !result.isEmpty()) {
            Object[] row = result.get(0);
            return new StatisticsReportDTO(
                    row[0] != null ? ((Number) row[0]).longValue() : 0L,
                    row[1] != null ? ((Number) row[1]).longValue() : 0L,
                    row[2] != null ? ((Number) row[2]).longValue() : 0L,
                    row[3] != null ? ((Number) row[3]).longValue() : 0L,
                    row[4] != null ? ((Number) row[4]).longValue() : 0L,
                    row[5] != null ? ((Number) row[5]).longValue() : 0L
            );
        }
        return new StatisticsReportDTO(0L, 0L, 0L, 0L, 0L, 0L);
    }

    @Override
    public List<TeacherReportDTO> getDocentesReporte(Integer idCarrera, Integer idPeriodo) {
        Integer pPeriodo = (idPeriodo != null) ? idPeriodo : 0;
        List<Object[]> results = teacherRepository.callDocentesReporteFunction(idCarrera, pPeriodo);

        return results.stream().map(row -> new TeacherReportDTO(
                row[0] != null ? ((Number) row[0]).longValue() : null,
                row[1] != null ? row[1].toString() : "N/A",
                row[2] != null ? row[2].toString() : "General",
                row[3] != null ? ((Number) row[3]).longValue() : 0L
        )).collect(Collectors.toList());
    }

    @Override
    public List<StudentReportDTO> getEstudiantesReporte(Integer idCarrera, Integer idPeriodo, String estado, Integer idDirector) {
        Integer pPeriodo = (idPeriodo != null) ? idPeriodo : 0;
        String pEstado = (estado != null && !estado.isEmpty() && !estado.equalsIgnoreCase("Todos los estados")) ? estado : "";
        Integer pDirector = (idDirector != null) ? idDirector : 0;

        List<Object[]> results = studentRepository.callEstudiantesReporteFunction(idCarrera, pPeriodo, pEstado, pDirector);

        return results.stream().map(row -> new StudentReportDTO(
                row[0] != null ? ((Number) row[0]).longValue() : null,
                row[1] != null ? row[1].toString() : "",
                row[2] != null ? row[2].toString() : "",
                row[3] != null ? row[3].toString() : "Sin asignar",
                row[4] != null ? row[4].toString() : "",
                row[5] != null ? row[5].toString() : ""
        )).collect(Collectors.toList());
    }
}