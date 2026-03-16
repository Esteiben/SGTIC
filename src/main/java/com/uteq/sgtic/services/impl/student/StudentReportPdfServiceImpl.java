package com.uteq.sgtic.services.impl.student;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.uteq.sgtic.dtos.student.reports.*;
import com.uteq.sgtic.services.student.IStudentReportPdfService;
import com.uteq.sgtic.services.student.IStudentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentReportPdfServiceImpl implements IStudentReportPdfService {

    private final IStudentReportService studentReportService;

    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    private static final Font SECTION_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    private static final Font NORMAL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10);
    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public byte[] generateReportPdf(StudentReportType reportType) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate(), 36, 36, 48, 36);
            PdfWriter.getInstance(document, out);
            document.open();

            StudentAuthContextDTO ctx = studentReportService.getAuthenticatedStudentContext();
            addDocumentHeader(document, buildTitle(reportType), ctx);

            switch (reportType) {
                case GENERAL_STATUS -> addGeneralStatus(document);
                case TITULATION_OPTION -> addTitulationOption(document);
                case TOPIC_OR_PROPOSAL -> addTopicOrProposal(document);
                case TOPIC_CHANGE_HISTORY -> addTopicChangeHistory(document);
                case PROPOSAL_VERSION_HISTORY -> addProposalVersionHistory(document);
                case DIRECTOR_ASSIGNMENT -> addDirectorAssignment(document);
                case THESIS_PROGRESS -> addThesisProgress(document);
                case TUTORING_HISTORY -> addTutoringHistory(document);
                case ADMISSION_REQUEST -> addAdmissionRequest(document);
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de reporte no soportado.");
            }

            document.close();
            return out.toByteArray();

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo generar el PDF del reporte.",
                    ex
            );
        }
    }

    @Override
    public String buildFilename(StudentReportType reportType) {
        return switch (reportType) {
            case GENERAL_STATUS -> "reporte_estado_general_estudiante.pdf";
            case TITULATION_OPTION -> "reporte_opcion_titulacion_estudiante.pdf";
            case TOPIC_OR_PROPOSAL -> "reporte_tema_o_propuesta_estudiante.pdf";
            case TOPIC_CHANGE_HISTORY -> "reporte_historial_cambios_tema_estudiante.pdf";
            case PROPOSAL_VERSION_HISTORY -> "reporte_historial_versiones_propuesta_estudiante.pdf";
            case DIRECTOR_ASSIGNMENT -> "reporte_asignacion_director_estudiante.pdf";
            case THESIS_PROGRESS -> "reporte_seguimiento_trabajo_titulacion_estudiante.pdf";
            case TUTORING_HISTORY -> "reporte_tutorias_estudiante.pdf";
            case ADMISSION_REQUEST -> "reporte_solicitud_ingreso_estudiante.pdf";
        };
    }

    private String buildTitle(StudentReportType reportType) {
        return switch (reportType) {
            case GENERAL_STATUS -> "Reporte de Estado General del Proceso";
            case TITULATION_OPTION -> "Reporte de Opción de Titulación";
            case TOPIC_OR_PROPOSAL -> "Reporte de Tema o Propuesta Actual";
            case TOPIC_CHANGE_HISTORY -> "Reporte de Historial de Cambios de Tema";
            case PROPOSAL_VERSION_HISTORY -> "Reporte de Historial de Versiones de Propuesta";
            case DIRECTOR_ASSIGNMENT -> "Reporte de Asignación de Director";
            case THESIS_PROGRESS -> "Reporte de Seguimiento del Trabajo de Titulación";
            case TUTORING_HISTORY -> "Reporte de Tutorías";
            case ADMISSION_REQUEST -> "Reporte de Solicitud de Ingreso";
        };
    }

    private void addDocumentHeader(Document document, String title, StudentAuthContextDTO ctx) throws Exception {
        Paragraph titleParagraph = new Paragraph(title, TITLE_FONT);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(titleParagraph);
        document.add(new Paragraph(" "));

        Map<String, String> headerData = new LinkedHashMap<>();
        headerData.put("Estudiante", safe(ctx.getFullName()));
        headerData.put("Correo", safe(ctx.getEmail()));
        headerData.put("Carrera", safe(ctx.getCareerName()));
        headerData.put("Período académico", safe(ctx.getAcademicPeriodName()));
        headerData.put("Estado titulación", safe(ctx.getTitulationStatus()));
        addKeyValueTable(document, headerData);
        document.add(new Paragraph(" "));
    }

    private void addGeneralStatus(Document document) throws Exception {
        StudentGeneralStatusReportDTO dto = studentReportService.getGeneralStatusReport();

        Map<String, String> data = new LinkedHashMap<>();
        data.put("ID estudiante", safe(dto.getStudentId()));
        data.put("Estudiante", safe(dto.getStudentFullName()));
        data.put("Correo", safe(dto.getStudentEmail()));
        data.put("Carrera", safe(dto.getCareerName()));
        data.put("Período académico", safe(dto.getAcademicPeriodName()));
        data.put("Estado titulación", safe(dto.getTitulationStatus()));
        data.put("Opción actual", safe(dto.getCurrentTitulationOption()));
        data.put("Fecha selección opción", safeDate(dto.getOptionSelectionDate()));
        data.put("Tipo período titulación", safe(dto.getTitulationPeriodType()));
        data.put("Aprobado", safeBoolean(dto.getTitulationPeriodApproved()));
        data.put("Fecha aprobación", safeDate(dto.getApprovalDate()));
        data.put("Observaciones", safe(dto.getObservations()));

        addSectionTitle(document, "Detalle");
        addKeyValueTable(document, data);
    }

    private void addTitulationOption(Document document) throws Exception {
        StudentOptionReportDTO dto = studentReportService.getTitulationOptionReport();

        Map<String, String> data = new LinkedHashMap<>();
        data.put("ID estudiante", safe(dto.getStudentId()));
        data.put("Opción actual", safe(dto.getCurrentOptionName()));
        data.put("Descripción", safe(dto.getCurrentOptionDescription()));
        data.put("Fecha selección", safeDate(dto.getSelectionDate()));
        data.put("Última fecha de cambio", safeDate(dto.getLastChangeDate()));
        data.put("Opción anterior", safe(dto.getPreviousOptionName()));
        data.put("Motivo del cambio", safe(dto.getChangeReason()));

        addSectionTitle(document, "Detalle");
        addKeyValueTable(document, data);
    }

    private void addTopicOrProposal(Document document) throws Exception {
        StudentTopicReportDTO dto = studentReportService.getTopicOrProposalReport();

        Map<String, String> data = new LinkedHashMap<>();
        data.put("ID propuesta", safe(dto.getProposalId()));
        data.put("Origen", safe(dto.getTopicSource()));
        data.put("Título", safe(dto.getTopicTitle()));
        data.put("Descripción", safe(dto.getTopicDescription()));
        data.put("Opción titulación", safe(dto.getTitulationOption()));
        data.put("Período académico", safe(dto.getAcademicPeriodName()));
        data.put("Fecha envío", safeDate(dto.getProposalSubmissionDate()));
        data.put("Estado propuesta", safe(dto.getProposalStatus()));
        data.put("Observaciones propuesta", safe(dto.getProposalObservations()));
        data.put("Comisión", safe(dto.getCommissionName()));
        data.put("Estado tema", safe(dto.getTopicStatus()));
        data.put("URL documento", safe(dto.getDocumentUrl()));
        data.put("Feedback docente", safe(dto.getTeacherFeedback()));
        data.put("Versión", safe(dto.getVersionNumber()));

        addSectionTitle(document, "Detalle");
        addKeyValueTable(document, data);
    }

    private void addTopicChangeHistory(Document document) throws Exception {
        List<StudentTopicChangeHistoryItemDTO> items = studentReportService.getTopicChangeHistoryReport();

        addSectionTitle(document, "Historial");
        if (items.isEmpty()) {
            document.add(new Paragraph("No existen cambios de tema registrados.", NORMAL_FONT));
            return;
        }

        PdfPTable table = new PdfPTable(new float[]{1.1f, 1.8f, 2.2f, 2.2f, 1.7f, 1.7f, 1.5f, 2.4f});
        table.setWidthPercentage(100);

        addTableHeader(table, "ID");
        addTableHeader(table, "Período");
        addTableHeader(table, "Tema anterior");
        addTableHeader(table, "Tema nuevo");
        addTableHeader(table, "Opción anterior");
        addTableHeader(table, "Opción nueva");
        addTableHeader(table, "Fecha");
        addTableHeader(table, "Motivo");

        for (StudentTopicChangeHistoryItemDTO item : items) {
            addTableCell(table, safe(item.getChangeId()));
            addTableCell(table, safe(item.getAcademicPeriodName()));
            addTableCell(table, safe(item.getPreviousTopicTitle()));
            addTableCell(table, safe(item.getNewTopicTitle()));
            addTableCell(table, safe(item.getPreviousOptionName()));
            addTableCell(table, safe(item.getNewOptionName()));
            addTableCell(table, safeDate(item.getChangeDate()));
            addTableCell(table, safe(item.getReason()));
        }

        document.add(table);
    }

    private void addProposalVersionHistory(Document document) throws Exception {
        List<StudentProposalVersionHistoryItemDTO> items = studentReportService.getProposalVersionHistoryReport();

        addSectionTitle(document, "Historial");
        if (items.isEmpty()) {
            document.add(new Paragraph("No existen versiones de propuesta registradas.", NORMAL_FONT));
            return;
        }

        PdfPTable table = new PdfPTable(new float[]{1f, 1f, 1f, 2.2f, 1.5f, 1.5f, 1.7f, 1.7f, 2.2f});
        table.setWidthPercentage(100);

        addTableHeader(table, "Hist.");
        addTableHeader(table, "Prop.");
        addTableHeader(table, "Versión");
        addTableHeader(table, "Título");
        addTableHeader(table, "Estado prop.");
        addTableHeader(table, "Estado tema");
        addTableHeader(table, "Fecha envío");
        addTableHeader(table, "Fecha cambio");
        addTableHeader(table, "Motivo");

        for (StudentProposalVersionHistoryItemDTO item : items) {
            addTableCell(table, safe(item.getHistoryId()));
            addTableCell(table, safe(item.getProposalId()));
            addTableCell(table, safe(item.getVersionNumber()));
            addTableCell(table, safe(item.getTitle()));
            addTableCell(table, safe(item.getProposalStatus()));
            addTableCell(table, safe(item.getTopicStatus()));
            addTableCell(table, safeDate(item.getSubmissionDate()));
            addTableCell(table, safeDateTime(item.getChangeDate()));
            addTableCell(table, safe(item.getChangeReason()));
        }

        document.add(table);
    }

    private void addDirectorAssignment(Document document) throws Exception {
        StudentDirectorAssignmentReportDTO dto = studentReportService.getDirectorAssignmentReport();

        Map<String, String> data = new LinkedHashMap<>();
        data.put("ID propuesta", safe(dto.getProposalId()));
        data.put("ID asignación", safe(dto.getAssignmentId()));
        data.put("Fecha asignación", safeDate(dto.getAssignmentDate()));
        data.put("Respuesta", safe(dto.getResponse()));
        data.put("Observaciones", safe(dto.getObservations()));
        data.put("ID director", safe(dto.getDirectorId()));
        data.put("Director", safe(dto.getDirectorFullName()));
        data.put("Correo director", safe(dto.getDirectorEmail()));
        data.put("Tema", safe(dto.getTopicTitle()));
        data.put("Estado propuesta", safe(dto.getProposalStatus()));

        addSectionTitle(document, "Detalle");
        addKeyValueTable(document, data);
    }

    private void addThesisProgress(Document document) throws Exception {
        StudentThesisProgressReportDTO dto = studentReportService.getThesisProgressReport();

        Map<String, String> data = new LinkedHashMap<>();
        data.put("ID trabajo", safe(dto.getThesisWorkId()));
        data.put("ID propuesta", safe(dto.getProposalId()));
        data.put("Estado trabajo", safe(dto.getThesisStatus()));
        data.put("Período académico", safe(dto.getAcademicPeriodName()));
        data.put("Tema", safe(dto.getTopicTitle()));
        data.put("Fecha envío propuesta", safeDate(dto.getProposalSubmissionDate()));
        data.put("Estado propuesta", safe(dto.getProposalStatus()));
        data.put("ID director", safe(dto.getDirectorId()));
        data.put("Director", safe(dto.getDirectorFullName()));
        data.put("Correo director", safe(dto.getDirectorEmail()));

        addSectionTitle(document, "Detalle");
        addKeyValueTable(document, data);
    }

    private void addTutoringHistory(Document document) throws Exception {
        List<StudentTutoringItemDTO> items = studentReportService.getTutoringHistoryReport();

        addSectionTitle(document, "Historial");
        if (items.isEmpty()) {
            document.add(new Paragraph("No existen tutorías registradas.", NORMAL_FONT));
            return;
        }

        PdfPTable table = new PdfPTable(new float[]{1f, 1f, 1.7f, 1.5f, 1.5f, 2.4f, 1f, 1f, 2.5f, 2f});
        table.setWidthPercentage(100);

        addTableHeader(table, "ID");
        addTableHeader(table, "Trabajo");
        addTableHeader(table, "Fecha");
        addTableHeader(table, "Tipo");
        addTableHeader(table, "Modalidad");
        addTableHeader(table, "Lugar/Enlace");
        addTableHeader(table, "Asistencia");
        addTableHeader(table, "Registrada");
        addTableHeader(table, "Observaciones");
        addTableHeader(table, "Director");

        for (StudentTutoringItemDTO item : items) {
            addTableCell(table, safe(item.getTutoringId()));
            addTableCell(table, safe(item.getThesisWorkId()));
            addTableCell(table, safeDateTime(item.getTutoringDate()));
            addTableCell(table, safe(item.getTutoringType()));
            addTableCell(table, safe(item.getModality()));
            addTableCell(table, safe(item.getLocationOrLink()));
            addTableCell(table, safeBoolean(item.getAttendance()));
            addTableCell(table, safeBoolean(item.getRegistered()));
            addTableCell(table, safe(item.getObservations()));
            addTableCell(table, safe(item.getDirectorFullName()));
        }

        document.add(table);
    }

    private void addAdmissionRequest(Document document) throws Exception {
        StudentAdmissionRequestReportDTO dto = studentReportService.getAdmissionRequestReport();

        Map<String, String> data = new LinkedHashMap<>();
        data.put("ID solicitud", safe(dto.getAdmissionRequestId()));
        data.put("Identificación", safe(dto.getIdentification()));
        data.put("Nombres", safe(dto.getFirstNames()));
        data.put("Apellidos", safe(dto.getLastNames()));
        data.put("Correo", safe(dto.getEmail()));
        data.put("Facultad", safe(dto.getFacultyName()));
        data.put("Carrera", safe(dto.getCareerName()));
        data.put("Período académico", safe(dto.getAcademicPeriodName()));
        data.put("Fecha envío", safeDate(dto.getSubmissionDate()));
        data.put("Estado", safe(dto.getStatus()));
        data.put("Observaciones", safe(dto.getObservations()));
        data.put("Fecha respuesta", safeDate(dto.getResponseDate()));

        addSectionTitle(document, "Detalle");
        addKeyValueTable(document, data);
    }

    private void addSectionTitle(Document document, String title) throws Exception {
        Paragraph paragraph = new Paragraph(title, SECTION_FONT);
        paragraph.setSpacingBefore(6);
        paragraph.setSpacingAfter(8);
        document.add(paragraph);
    }

    private void addKeyValueTable(Document document, Map<String, String> data) throws Exception {
        PdfPTable table = new PdfPTable(new float[]{2f, 5f});
        table.setWidthPercentage(100);

        for (Map.Entry<String, String> entry : data.entrySet()) {
            addTableHeader(table, entry.getKey());
            addTableCell(table, entry.getValue());
        }

        document.add(table);
    }

    private void addTableHeader(PdfPTable table, String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value, HEADER_FONT));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value, NORMAL_FONT));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        table.addCell(cell);
    }

    private String safe(Object value) {
        return value == null ? "N/A" : String.valueOf(value);
    }

    private String safeDate(java.time.LocalDate value) {
        return value == null ? "N/A" : value.format(DATE_FORMAT);
    }

    private String safeDateTime(java.time.LocalDateTime value) {
        return value == null ? "N/A" : value.format(DATETIME_FORMAT);
    }

    private String safeBoolean(Boolean value) {
        if (value == null) return "N/A";
        return value ? "Sí" : "No";
    }
}