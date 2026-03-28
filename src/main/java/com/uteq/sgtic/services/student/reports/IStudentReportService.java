package com.uteq.sgtic.services.student.reports;

import java.util.List;
import com.uteq.sgtic.dtos.student.reports.ReportCatalogItemDTO;

public interface IStudentReportService {
    List<ReportCatalogItemDTO> getCatalog();
    String getReportData(Long idUsuario, Long idPeriodo, String reportType);
    byte[] generatePdfReport(Long idUsuario, Long idPeriodo, String reportType);
}
