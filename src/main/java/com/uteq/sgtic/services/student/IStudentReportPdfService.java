package com.uteq.sgtic.services.student;

import com.uteq.sgtic.dtos.student.reports.StudentReportType;

public interface IStudentReportPdfService {
    byte[] generateReportPdf(StudentReportType reportType);
    String buildFilename(StudentReportType reportType);
}