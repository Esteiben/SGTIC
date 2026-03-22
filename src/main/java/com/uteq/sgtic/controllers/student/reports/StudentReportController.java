package com.uteq.sgtic.controllers.student.reports;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uteq.sgtic.dtos.student.reports.ReportCatalogItemDTO;
import com.uteq.sgtic.services.student.reports.IStudentReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student-reports")
@RequiredArgsConstructor
public class StudentReportController {

    private final IStudentReportService reportService;

    @GetMapping("/catalog")
    public ResponseEntity<List<ReportCatalogItemDTO>> getCatalog() {
        return ResponseEntity.ok(reportService.getCatalog());
    }

    // El frontend pide datos crudos que se renderizan en tablas dinámicas
    @GetMapping("/data/{reportType}")
    public ResponseEntity<String> getReportData(
            @PathVariable String reportType,
            @RequestParam("periodoId") Long periodoId
            // Principal principal / @AuthenticationPrincipal (Si usas Spring Security para sacar el ID del usuario)
    ) {
        // MOCK: Suponiendo que el usuario logueado en tu sistema es Juan Carlos (id_usuario = 13)
        // En producción extraerías esto del Token JWT
        Long currentUserId = 13L; 

        String jsonData = reportService.getReportData(currentUserId, periodoId, reportType);
        
        // Retornamos el JSON directamente, asegurando el tipo de contenido
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(jsonData);
    }

    @GetMapping("/pdf/{reportType}")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable String reportType,
            @RequestParam("periodoId") Long periodoId
    ) {
        Long currentUserId = 13L; // MOCK: Cambiar luego por el del token JWT
        byte[] pdfBytes = reportService.generatePdfReport(currentUserId, periodoId, reportType);
        
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=reporte_" + reportType + ".pdf")
                .header("Content-Type", "application/pdf")
                .body(pdfBytes);    
    }
}