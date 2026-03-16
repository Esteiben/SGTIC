package com.uteq.sgtic.controllers.student;

import com.uteq.sgtic.dtos.student.reports.*;
import com.uteq.sgtic.services.student.IStudentReportPdfService;
import com.uteq.sgtic.services.student.IStudentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/reports")
@RequiredArgsConstructor
public class StudentReportController {

    private final IStudentReportService studentReportService;
    private final IStudentReportPdfService studentReportPdfService;

    @GetMapping("/catalog")
    public ResponseEntity<List<ReportCatalogItemDTO>> getCatalog() {
        return ResponseEntity.ok(studentReportService.getAvailableReports());
    }

    @GetMapping("/general-status")
    public ResponseEntity<StudentGeneralStatusReportDTO> getGeneralStatusReport() {
        return ResponseEntity.ok(studentReportService.getGeneralStatusReport());
    }

    @GetMapping("/titulation-option")
    public ResponseEntity<StudentOptionReportDTO> getTitulationOptionReport() {
        return ResponseEntity.ok(studentReportService.getTitulationOptionReport());
    }

    @GetMapping("/topic-or-proposal")
    public ResponseEntity<StudentTopicReportDTO> getTopicOrProposalReport() {
        return ResponseEntity.ok(studentReportService.getTopicOrProposalReport());
    }

    @GetMapping("/topic-change-history")
    public ResponseEntity<List<StudentTopicChangeHistoryItemDTO>> getTopicChangeHistoryReport() {
        return ResponseEntity.ok(studentReportService.getTopicChangeHistoryReport());
    }

    @GetMapping("/proposal-version-history")
    public ResponseEntity<List<StudentProposalVersionHistoryItemDTO>> getProposalVersionHistoryReport() {
        return ResponseEntity.ok(studentReportService.getProposalVersionHistoryReport());
    }

    @GetMapping("/director-assignment")
    public ResponseEntity<StudentDirectorAssignmentReportDTO> getDirectorAssignmentReport() {
        return ResponseEntity.ok(studentReportService.getDirectorAssignmentReport());
    }

    @GetMapping("/thesis-progress")
    public ResponseEntity<StudentThesisProgressReportDTO> getThesisProgressReport() {
        return ResponseEntity.ok(studentReportService.getThesisProgressReport());
    }

    @GetMapping("/tutoring-history")
    public ResponseEntity<List<StudentTutoringItemDTO>> getTutoringHistoryReport() {
        return ResponseEntity.ok(studentReportService.getTutoringHistoryReport());
    }

    @GetMapping("/admission-request")
    public ResponseEntity<StudentAdmissionRequestReportDTO> getAdmissionRequestReport() {
        return ResponseEntity.ok(studentReportService.getAdmissionRequestReport());
    }

    @GetMapping("/{reportType}/pdf")
    public ResponseEntity<byte[]> downloadReportPdf(@PathVariable StudentReportType reportType) {
        byte[] pdf = studentReportPdfService.generateReportPdf(reportType);
        String filename = studentReportPdfService.buildFilename(reportType);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(filename).build().toString()
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}