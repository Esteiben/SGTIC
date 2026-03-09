package com.uteq.sgtic.services.impl.student;

import com.uteq.sgtic.dtos.student.SaveTopicSelectionRequestDTO;
import com.uteq.sgtic.dtos.student.SaveTopicSelectionResponseDTO;
import com.uteq.sgtic.repository.student.StudentTopicSelectionRepository;
import com.uteq.sgtic.services.student.IStudentTopicSelectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentTopicSelectionServiceImpl implements IStudentTopicSelectionService {

    private final StudentTopicSelectionRepository repository;

    @Override
    public SaveTopicSelectionResponseDTO saveSelection(
            Integer userId,
            Integer idCarrera,
            SaveTopicSelectionRequestDTO request
    ) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        if (request.getIdTema() == null || request.getIdOpcion() == null || request.getIdPeriodo() == null) {
            throw new IllegalArgumentException("Debe enviar idTema, idOpcion e idPeriodo");
        }

        Integer idEstudiante = repository.findStudentIdByUserId(userId);
        if (idEstudiante == null) {
            throw new IllegalArgumentException("No se encontró el estudiante asociado al usuario");
        }

        if (!repository.existsActivePeriod(request.getIdPeriodo())) {
            throw new IllegalStateException("El período seleccionado no está activo o no existe");
        }

        LocalDate fechaInicioPeriodo = repository.findPeriodStartDate(request.getIdPeriodo());
        if (fechaInicioPeriodo == null) {
            throw new IllegalArgumentException("No se encontró la fecha de inicio del período");
        }

        LocalDate fechaLimiteSeleccion = fechaInicioPeriodo.plusWeeks(4);
        if (LocalDate.now().isAfter(fechaLimiteSeleccion)) {
            throw new IllegalStateException(
                    "La fecha límite para seleccionar tema ya expiró. Fecha límite: " + fechaLimiteSeleccion
            );
        }

        boolean temaDisponible = repository.existsAvailableTopic(
                request.getIdTema(),
                idCarrera,
                request.getIdOpcion()
        );

        if (!temaDisponible) {
            throw new IllegalArgumentException("El tema seleccionado no está disponible para la carrera u opción indicada");
        }

        if (repository.existsStudentOptionSelection(idEstudiante)
                || repository.existsActiveProposalByStudent(idEstudiante)
                || repository.existsWorkByStudent(idEstudiante)) {
            throw new IllegalStateException("El estudiante ya tiene una selección o propuesta registrada");
        }

        repository.insertStudentOption(idEstudiante, request.getIdOpcion());
        repository.insertSelectedTopicProposal(idEstudiante, request.getIdTema(), request.getIdPeriodo());

        return new SaveTopicSelectionResponseDTO(
                "Selección guardada correctamente",
                request.getIdTema(),
                request.getIdOpcion(),
                request.getIdPeriodo(),
                fechaLimiteSeleccion
        );
    }
}