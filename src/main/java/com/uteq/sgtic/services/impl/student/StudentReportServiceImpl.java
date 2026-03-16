package com.uteq.sgtic.services.impl.student;

import com.uteq.sgtic.dtos.student.reports.*;
import com.uteq.sgtic.repository.student.StudentReportRepository;
import com.uteq.sgtic.services.student.IStudentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentReportServiceImpl implements IStudentReportService {

    private final StudentReportRepository studentReportRepository;

    @Override
    public List<ReportCatalogItemDTO> getAvailableReports() {
        return List.of(
                new ReportCatalogItemDTO(
                        StudentReportType.GENERAL_STATUS,
                        "Estado general del proceso",
                        "Resumen del estado del proceso de titulación del estudiante.",
                        "/api/student/reports/general-status",
                        "/api/student/reports/GENERAL_STATUS/pdf"
                ),
                new ReportCatalogItemDTO(
                        StudentReportType.TITULATION_OPTION,
                        "Opción de titulación",
                        "Detalle de la opción de titulación elegida por el estudiante.",
                        "/api/student/reports/titulation-option",
                        "/api/student/reports/TITULATION_OPTION/pdf"
                ),
                new ReportCatalogItemDTO(
                        StudentReportType.TOPIC_OR_PROPOSAL,
                        "Tema o propuesta actual",
                        "Muestra el tema actual del banco o la propuesta del estudiante.",
                        "/api/student/reports/topic-or-proposal",
                        "/api/student/reports/TOPIC_OR_PROPOSAL/pdf"
                ),
                new ReportCatalogItemDTO(
                        StudentReportType.TOPIC_CHANGE_HISTORY,
                        "Historial de cambios de tema",
                        "Lista los cambios de tema realizados por el estudiante.",
                        "/api/student/reports/topic-change-history",
                        "/api/student/reports/TOPIC_CHANGE_HISTORY/pdf"
                ),
                new ReportCatalogItemDTO(
                        StudentReportType.PROPOSAL_VERSION_HISTORY,
                        "Historial de versiones de propuesta",
                        "Muestra las versiones registradas de la propuesta de tema.",
                        "/api/student/reports/proposal-version-history",
                        "/api/student/reports/PROPOSAL_VERSION_HISTORY/pdf"
                ),
                new ReportCatalogItemDTO(
                        StudentReportType.DIRECTOR_ASSIGNMENT,
                        "Asignación de director",
                        "Detalle de la asignación del director del trabajo de titulación.",
                        "/api/student/reports/director-assignment",
                        "/api/student/reports/DIRECTOR_ASSIGNMENT/pdf"
                ),
                new ReportCatalogItemDTO(
                        StudentReportType.THESIS_PROGRESS,
                        "Seguimiento del trabajo de titulación",
                        "Estado del trabajo de titulación y del director asignado.",
                        "/api/student/reports/thesis-progress",
                        "/api/student/reports/THESIS_PROGRESS/pdf"
                ),
                new ReportCatalogItemDTO(
                        StudentReportType.TUTORING_HISTORY,
                        "Tutorías",
                        "Historial de tutorías registradas para el estudiante.",
                        "/api/student/reports/tutoring-history",
                        "/api/student/reports/TUTORING_HISTORY/pdf"
                ),
                new ReportCatalogItemDTO(
                        StudentReportType.ADMISSION_REQUEST,
                        "Solicitud de ingreso",
                        "Estado de la solicitud de ingreso al proceso de titulación.",
                        "/api/student/reports/admission-request",
                        "/api/student/reports/ADMISSION_REQUEST/pdf"
                )
        );
    }

    @Override
    public StudentGeneralStatusReportDTO getGeneralStatusReport() {
        StudentAuthContextDTO ctx = getAuthenticatedStudentContext();
        return studentReportRepository.findGeneralStatusByStudentId(ctx.getStudentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró información del estado general del proceso."
                ));
    }

    @Override
    public StudentOptionReportDTO getTitulationOptionReport() {
        StudentAuthContextDTO ctx = getAuthenticatedStudentContext();
        return studentReportRepository.findOptionReportByStudentId(ctx.getStudentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la opción de titulación del estudiante."
                ));
    }

    @Override
    public StudentTopicReportDTO getTopicOrProposalReport() {
        StudentAuthContextDTO ctx = getAuthenticatedStudentContext();
        return studentReportRepository.findCurrentTopicReportByStudentId(ctx.getStudentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró un tema o propuesta registrada para el estudiante."
                ));
    }

    @Override
    public List<StudentTopicChangeHistoryItemDTO> getTopicChangeHistoryReport() {
        StudentAuthContextDTO ctx = getAuthenticatedStudentContext();
        return studentReportRepository.findTopicChangeHistoryByStudentId(ctx.getStudentId());
    }

    @Override
    public List<StudentProposalVersionHistoryItemDTO> getProposalVersionHistoryReport() {
        StudentAuthContextDTO ctx = getAuthenticatedStudentContext();
        return studentReportRepository.findProposalVersionHistoryByStudentId(ctx.getStudentId());
    }

    @Override
    public StudentDirectorAssignmentReportDTO getDirectorAssignmentReport() {
        StudentAuthContextDTO ctx = getAuthenticatedStudentContext();
        return studentReportRepository.findDirectorAssignmentByStudentId(ctx.getStudentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró una propuesta actual para revisar asignación de director."
                ));
    }

    @Override
    public StudentThesisProgressReportDTO getThesisProgressReport() {
        StudentAuthContextDTO ctx = getAuthenticatedStudentContext();

        return studentReportRepository.findThesisProgressByStudentId(ctx.getStudentId())
                .orElseGet(() -> {
                    StudentThesisProgressReportDTO dto = new StudentThesisProgressReportDTO();
                    dto.setThesisWorkId(null);
                    dto.setProposalId(null);
                    dto.setThesisStatus("SIN_TRABAJO_REGISTRADO");
                    dto.setAcademicPeriodName(ctx.getAcademicPeriodName());
                    dto.setTopicTitle(null);
                    dto.setProposalSubmissionDate(null);
                    dto.setProposalStatus(null);
                    dto.setDirectorId(null);
                    dto.setDirectorFullName(null);
                    dto.setDirectorEmail(null);
                    return dto;
                });
    }

    @Override
    public List<StudentTutoringItemDTO> getTutoringHistoryReport() {
        StudentAuthContextDTO ctx = getAuthenticatedStudentContext();
        return studentReportRepository.findTutoringHistoryByStudentId(ctx.getStudentId());
    }

    @Override
    public StudentAdmissionRequestReportDTO getAdmissionRequestReport() {
        StudentAuthContextDTO ctx = getAuthenticatedStudentContext();
        return studentReportRepository.findLatestAdmissionRequestByEmail(ctx.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró una solicitud de ingreso asociada al estudiante autenticado."
                ));
    }

    @Override
    public StudentAuthContextDTO getAuthenticatedStudentContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equalsIgnoreCase(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado.");
        }

        String email = authentication.getName();

        return studentReportRepository.findStudentContextByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "El usuario autenticado no tiene un perfil de estudiante válido."
                ));
    }
}