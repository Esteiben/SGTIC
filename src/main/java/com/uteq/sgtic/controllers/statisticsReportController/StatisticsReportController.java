package com.uteq.sgtic.controllers.statisticsReportController;

import com.uteq.sgtic.dtos.AcademicPeriodDTO;
import com.uteq.sgtic.dtos.statisticsReportDTO.*;
import com.uteq.sgtic.services.IStatisticsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class StatisticsReportController {

    @Autowired
    private IStatisticsReportService reportService;

    @GetMapping("/carrera-nombre/{id}")
    public ResponseEntity<String> getNombreCarrera(@PathVariable Integer id) {
        return ResponseEntity.ok(reportService.getNombreCarrera(id));
    }

    @GetMapping("/periodos")
    public ResponseEntity<List<AcademicPeriodDTO>> getPeriodos() {
        return ResponseEntity.ok(reportService.getPeriodos());
    }

    @GetMapping("/estados")
    public ResponseEntity<List<String>> getEstados() {
        return ResponseEntity.ok(reportService.getEstadosTesis());
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<StatisticsReportDTO> getEstadisticas(
            @RequestParam Integer idCarrera,
            @RequestParam(required = false) Integer idPeriodo) {
        return ResponseEntity.ok(reportService.getEstadisticas(idCarrera, idPeriodo));
    }

    @GetMapping("/docentes")
    public ResponseEntity<List<TeacherReportDTO>> getDocentesReporte(
            @RequestParam Integer idCarrera,
            @RequestParam(required = false) Integer idPeriodo) {
        return ResponseEntity.ok(reportService.getDocentesReporte(idCarrera, idPeriodo));
    }

    @GetMapping("/estudiantes")
    public ResponseEntity<List<StudentReportDTO>> getEstudiantesReporte(
            @RequestParam Integer idCarrera,
            @RequestParam(required = false) Integer idPeriodo,
            @RequestParam(required = false, defaultValue = "") String estado,
            @RequestParam(required = false) Integer idDirector) {
        return ResponseEntity.ok(reportService.getEstudiantesReporte(idCarrera, idPeriodo, estado, idDirector));
    }
}