package com.uteq.sgtic.services.impl.student;

import com.uteq.sgtic.dtos.student.*;
import com.uteq.sgtic.repository.student.StudentTopicSelectionRepository;
import com.uteq.sgtic.services.AzureStorageConfig;
import com.uteq.sgtic.services.student.IStudentTopicSelectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentTopicSelectionServiceImpl implements IStudentTopicSelectionService {

    private final StudentTopicSelectionRepository repository;
    private final AzureStorageConfig azureStorageConfig;

    @Override
    public SaveTopicSelectionResponseDTO saveSelection(
            Integer userId,
            Integer idCarrera,
            SaveTopicSelectionRequestDTO request
    ) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula.");
        }

        if (request.getIdTema() == null || request.getIdOpcion() == null || request.getIdPeriodo() == null) {
            throw new IllegalArgumentException("Debe enviar idTema, idOpcion e idPeriodo.");
        }

        Integer idEstudiante = repository.findStudentIdByUserId(userId);
        if (idEstudiante == null) {
            throw new IllegalArgumentException("No se encontró el estudiante asociado al usuario.");
        }

        LocalDate fechaLimite = repository.findSelectionDeadline(request.getIdPeriodo());

        try {
            repository.saveBankTopicSelection(
                    userId,
                    idCarrera,
                    request.getIdPeriodo(),
                    request.getIdTema(),
                    request.getIdOpcion()
            );

            Integer cambios = repository.countTopicChangesByStudentAndPeriod(idEstudiante, request.getIdPeriodo());
            boolean cambioTema = cambios != null && cambios > 0;

            return new SaveTopicSelectionResponseDTO(
                    cambioTema
                            ? "Tema cambiado correctamente. Se reinició la fase de asignación de director."
                            : "Selección guardada correctamente. Se generó el proceso para asignación de director.",
                    request.getIdTema(),
                    request.getIdOpcion(),
                    request.getIdPeriodo(),
                    fechaLimite,
                    cambioTema
            );

        } catch (DataAccessException e) {
            throw new IllegalStateException(extractDatabaseErrorMessage(e));
        }
    }

    @Override
    public RegisterProposalStudentTopicResponseDTO registerProposal(
            Integer userId,
            Integer idCarrera,
            RegisterProposalStudentTopicRequestDTO request
    ) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula.");
        }

        if (request.getIdOpcion() == null || request.getIdPeriodo() == null) {
            throw new IllegalArgumentException("Debe enviar idOpcion e idPeriodo.");
        }

        if (request.getTitulo() == null || request.getTitulo().trim().isBlank()) {
            throw new IllegalArgumentException("El título de la propuesta es obligatorio.");
        }

        if (request.getDescripcion() == null || request.getDescripcion().trim().isBlank()) {
            throw new IllegalArgumentException("La descripción de la propuesta es obligatoria.");
        }

        Integer idEstudiante = repository.findStudentIdByUserId(userId);
        if (idEstudiante == null) {
            throw new IllegalArgumentException("No se encontró el estudiante asociado al usuario.");
        }

        validateProposalFile(request.getDocumento());

        LocalDate fechaLimite = repository.findSelectionDeadline(request.getIdPeriodo());
        String urlDocumento = uploadProposalDocumentToAzure(request.getDocumento());

        try {
            Integer idPropuesta = repository.registerStudentTopicProposal(
                    userId,
                    request.getIdPeriodo(),
                    request.getIdOpcion(),
                    request.getTitulo().trim(),
                    request.getDescripcion().trim(),
                    urlDocumento
            );

            return new RegisterProposalStudentTopicResponseDTO(
                    "Propuesta registrada correctamente. Fue enviada al coordinador para su revisión.",
                    null,
                    idPropuesta,
                    request.getIdOpcion(),
                    request.getIdPeriodo(),
                    fechaLimite
            );

        } catch (DataAccessException e) {
            throw new IllegalStateException(extractDatabaseErrorMessage(e));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TopicSelectionStatusDTO getSelectionStatus(Integer userId, Integer idPeriodo) {
        if (idPeriodo == null) {
            throw new IllegalArgumentException("Debe enviar el idPeriodo.");
        }

        Integer idEstudiante = repository.findStudentIdByUserId(userId);
        if (idEstudiante == null) {
            throw new IllegalArgumentException("No se encontró el estudiante asociado al usuario.");
        }

        if (!repository.existsActivePeriod(idPeriodo)) {
            throw new IllegalStateException("El período seleccionado no está activo o no existe.");
        }

        LocalDate fechaLimite = repository.findSelectionDeadline(idPeriodo);
        boolean fueraDePlazo = LocalDate.now().isAfter(fechaLimite);

        var currentProposal = repository.findLatestActiveProposalByStudentAndPeriod(idEstudiante, idPeriodo);
        boolean tieneTrabajo = repository.existsWorkByStudentAndPeriod(idEstudiante, idPeriodo);
        boolean tieneProcesoTema = currentProposal != null || tieneTrabajo;

        String estadoTitulacion = repository.findStudentStatus(idEstudiante);
        boolean desactivadoPorPlazo = fueraDePlazo && !tieneProcesoTema;

        String tipoTemaActual = "NINGUNO";
        boolean tieneSeleccionBanco = false;
        boolean puedeSeleccionar = false;
        boolean puedeProponer = false;
        boolean puedeCambiarTema = false;

        Integer cambiosTemaRealizados = repository.countTopicChangesByStudentAndPeriod(idEstudiante, idPeriodo);
        if (cambiosTemaRealizados == null) {
            cambiosTemaRealizados = 0;
        }

        if (currentProposal == null) {
            puedeSeleccionar = !fueraDePlazo;
            puedeProponer = !fueraDePlazo;
        } else if (currentProposal.getIdTema() != null) {
            tipoTemaActual = "BANCO";
            tieneSeleccionBanco = true;
            puedeCambiarTema = !fueraDePlazo && cambiosTemaRealizados < 1;
        } else if (currentProposal.getIdTemaPropuesto() != null) {
            tipoTemaActual = "PROPUESTO";
        }

        String mensaje;
        if (desactivadoPorPlazo) {
            mensaje = "El estudiante fue desactivado para selección de tema porque no registró tema ni propuesta dentro de las primeras 4 semanas del período.";
        } else if (currentProposal == null && !fueraDePlazo) {
            mensaje = "Puedes seleccionar un tema del banco o registrar una propuesta propia hasta el " + fechaLimite + ".";
        } else if ("BANCO".equals(tipoTemaActual) && puedeCambiarTema) {
            mensaje = "Ya tienes una selección inicial del banco. Puedes cambiar de tema una sola vez hasta el " + fechaLimite + ".";
        } else if ("BANCO".equals(tipoTemaActual)) {
            mensaje = "Ya tienes un tema del banco registrado y no tienes más cambios disponibles.";
        } else if ("PROPUESTO".equals(tipoTemaActual)) {
            mensaje = "Ya tienes una propuesta de tema registrada y en proceso de revisión.";
        } else {
            mensaje = "No hay más acciones disponibles para este período.";
        }

        return new TopicSelectionStatusDTO(
                estadoTitulacion,
                tipoTemaActual,
                fueraDePlazo,
                desactivadoPorPlazo,
                tieneProcesoTema,
                tieneSeleccionBanco,
                puedeSeleccionar,
                puedeProponer,
                puedeCambiarTema,
                cambiosTemaRealizados,
                fechaLimite,
                mensaje
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentProposalSummaryDTO> getStudentProposals(Integer userId, Integer idPeriodo) {
        if (idPeriodo == null) {
            throw new IllegalArgumentException("Debe enviar el idPeriodo.");
        }

        Integer idEstudiante = repository.findStudentIdByUserId(userId);
        if (idEstudiante == null) {
            throw new IllegalArgumentException("No se encontró el estudiante asociado al usuario.");
        }

        return repository.findStudentProposalSummaries(idEstudiante, idPeriodo)
                .stream()
                .map(p -> new StudentProposalSummaryDTO(
                        p.getIdPropuesta(),
                        p.getIdTemaPropuesto(),
                        p.getTitulo(),
                        p.getDescripcion(),
                        p.getEstadoPropuesta(),
                        p.getEstadoTema(),
                        p.getFeedbackDocente(),
                        p.getUrlDocumento(),
                        p.getIdOpcion(),
                        p.getNombreOpcion(),
                        p.getNumeroVersion(),
                        p.getTotalVersiones(),
                        p.getFechaEnvio(),
                        p.getFechaUltimaActualizacion(),
                        Boolean.TRUE.equals(p.getEditable())
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentProposalHistoryItemDTO> getStudentProposalHistory(Integer userId, Integer idPropuesta) {
        if (idPropuesta == null) {
            throw new IllegalArgumentException("Debe enviar el idPropuesta.");
        }

        Integer idEstudiante = repository.findStudentIdByUserId(userId);
        if (idEstudiante == null) {
            throw new IllegalArgumentException("No se encontró el estudiante asociado al usuario.");
        }

        return repository.findStudentProposalHistory(idEstudiante, idPropuesta)
                .stream()
                .map(h -> new StudentProposalHistoryItemDTO(
                        h.getNumeroVersion(),
                        Boolean.TRUE.equals(h.getEsVersionActual()),
                        h.getTitulo(),
                        h.getDescripcion(),
                        h.getUrlDocumento(),
                        h.getIdOpcion(),
                        h.getNombreOpcion(),
                        h.getEstadoPropuesta(),
                        h.getEstadoTema(),
                        h.getFeedbackDocente(),
                        h.getFechaEnvio(),
                        h.getFechaMovimiento(),
                        h.getMotivo()
                ))
                .toList();
    }

    @Override
    public UpdateStudentProposalResponseDTO updateStudentProposal(
            Integer userId,
            Integer idPropuesta,
            UpdateStudentProposalRequestDTO request
    ) {
        if (idPropuesta == null) {
            throw new IllegalArgumentException("Debe enviar el idPropuesta.");
        }

        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula.");
        }

        if (request.getIdOpcion() == null) {
            throw new IllegalArgumentException("Debe enviar la opción de titulación.");
        }

        if (request.getTitulo() == null || request.getTitulo().trim().isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio.");
        }

        if (request.getDescripcion() == null || request.getDescripcion().trim().isBlank()) {
            throw new IllegalArgumentException("La descripción es obligatoria.");
        }

        validateProposalFile(request.getDocumento());
        String urlDocumento = uploadProposalDocumentToAzure(request.getDocumento());

        try {
            Integer numeroVersion = repository.updateStudentTopicProposal(
                    userId,
                    idPropuesta,
                    request.getIdOpcion(),
                    request.getTitulo().trim(),
                    request.getDescripcion().trim(),
                    urlDocumento
            );

            return new UpdateStudentProposalResponseDTO(
                    "Propuesta actualizada correctamente.",
                    idPropuesta,
                    numeroVersion
            );
        } catch (DataAccessException e) {
            throw new IllegalStateException(extractDatabaseErrorMessage(e));
        }
    }

    private void validateProposalFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return;
        }

        String originalName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        boolean valid = originalName.endsWith(".pdf")
                || originalName.endsWith(".doc")
                || originalName.endsWith(".docx");

        if (!valid) {
            throw new IllegalArgumentException("Solo se permiten archivos PDF, DOC o DOCX.");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("El archivo supera el tamaño máximo permitido de 10 MB.");
        }
    }

    private String uploadProposalDocumentToAzure(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            return azureStorageConfig.subirDocumento(file);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo subir el documento a Azure: " + e.getMessage(), e);
        }
    }

    private String extractDatabaseErrorMessage(Exception e) {
        Throwable current = e;

        while (current.getCause() != null) {
            current = current.getCause();
        }

        String message = current.getMessage();
        if (message == null || message.isBlank()) {
            return "Ocurrió un error al ejecutar la operación en base de datos.";
        }

        if (message.contains("ERROR:")) {
            int index = message.indexOf("ERROR:");
            return message.substring(index + 6).trim();
        }

        return message.trim();
    }
}