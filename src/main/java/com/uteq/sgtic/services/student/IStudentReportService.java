package com.uteq.sgtic.services.student;

import com.uteq.sgtic.dtos.student.reports.*;

import java.util.List;

public interface IStudentReportService {
    List<ReportCatalogItemDTO> getAvailableReports();

    StudentGeneralStatusReportDTO getGeneralStatusReport();
    StudentOptionReportDTO getTitulationOptionReport();
    StudentTopicReportDTO getTopicOrProposalReport();
    List<StudentTopicChangeHistoryItemDTO> getTopicChangeHistoryReport();
    List<StudentProposalVersionHistoryItemDTO> getProposalVersionHistoryReport();
    StudentDirectorAssignmentReportDTO getDirectorAssignmentReport();
    StudentThesisProgressReportDTO getThesisProgressReport();
    List<StudentTutoringItemDTO> getTutoringHistoryReport();
    StudentAdmissionRequestReportDTO getAdmissionRequestReport();

    StudentAuthContextDTO getAuthenticatedStudentContext();
}