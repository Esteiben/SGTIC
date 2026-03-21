package com.uteq.sgtic.services.impl.student.selectTopic;

import com.uteq.sgtic.dtos.student.selectTopic.TopicSelectionRequestDTO;
import com.uteq.sgtic.dtos.student.selectTopic.TopicSelectionResponseDTO;
import com.uteq.sgtic.repository.student.selectTopic.ProcessSetupRepository;
import com.uteq.sgtic.repository.student.selectTopic.TopicSelectionProjection;
import com.uteq.sgtic.repository.student.selectTopic.TopicSelectionRepository;
import com.uteq.sgtic.services.student.selectTopic.ITopicSelectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TopicSelectionServiceImpl implements ITopicSelectionService {

    private final TopicSelectionRepository repository;
    private final ProcessSetupRepository processSetupRepository;

    @Override
    @Transactional
    public TopicSelectionResponseDTO procesarSeleccionOCambio(Integer idEstudiante, TopicSelectionRequestDTO request) {

        Integer idMatricula = processSetupRepository.findIdMatricula(idEstudiante, request.getIdPeriodo());

        if (idMatricula == null) {
            return new TopicSelectionResponseDTO(false, "No se encontró matrícula del estudiante en el período seleccionado.");
        }

        TopicSelectionProjection result = repository.executeTopicSelection(
                idMatricula,
                request.getIdOpcion(),
                request.getIdTema(),
                request.getIdTemaPropuesto(),
                request.getMotivo() == null || request.getMotivo().isBlank()
                        ? "Selección Inicial"
                        : request.getMotivo()
        );

        return new TopicSelectionResponseDTO(
                result.getExito(),
                result.getMensaje()
        );
    }

    @Override
    @Transactional
    public TopicSelectionResponseDTO registrarPropuesta(Integer idEstudiante, Integer idPeriodo, Integer idOpcion, String titulo, String descripcion, String urlDocumento) {
        
        // 1. Buscamos la matrícula del estudiante en ese período
        Integer idMatricula = processSetupRepository.findIdMatricula(idEstudiante, idPeriodo);

        if (idMatricula == null) {
            return new TopicSelectionResponseDTO(false, "No se encontró matrícula del estudiante en el período seleccionado.");
        }

        // 2. Ejecutamos la función de PostgreSQL
        TopicSelectionProjection result = repository.registrarPropuesta(
                idMatricula, 
                idOpcion, 
                titulo, 
                descripcion, 
                urlDocumento
        );

        return new TopicSelectionResponseDTO(result.getExito(), result.getMensaje());
    }
}