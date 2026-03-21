package com.uteq.sgtic.services.impl.student.reports;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itextpdf.html2pdf.HtmlConverter;
import com.uteq.sgtic.dtos.student.reports.ReportCatalogItemDTO;
import com.uteq.sgtic.repository.student.reports.StudentReportRepository;
import com.uteq.sgtic.services.student.reports.IStudentReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentReportServiceImpl implements IStudentReportService {

    private final StudentReportRepository repository;

    @Override
    public List<ReportCatalogItemDTO> getCatalog() {
        return repository.getReportCatalog().stream().map(p -> new ReportCatalogItemDTO(
            p.getType(),
            p.getTitle(),
            p.getDescription(),
            p.getDataEndpoint(),
            p.getPdfEndpoint()
        )).collect(Collectors.toList());
    }

    @Override
    public String getReportData(Long idUsuario, Long idPeriodo, String reportType) {
        // La validación o reglas de negocio adicionales irían aquí
        return repository.getReportDataAsJson(idUsuario, idPeriodo, reportType);
    }

    @Override
    public byte[] generatePdfReport(Long idUsuario, Long idPeriodo, String reportType) {
        // 1. Traer el HTML crudo desde Postgres
        String htmlContext = repository.getReportPdfHtml(idUsuario, idPeriodo, reportType);
        
        // 2. Convertirlo a PDF
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContext, target);
        
        return target.toByteArray();
    }
}