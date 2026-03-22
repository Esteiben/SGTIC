package com.uteq.sgtic.services.student.selectTopic;

import com.uteq.sgtic.dtos.student.selectTopic.TopicSelectionRequestDTO;
import com.uteq.sgtic.dtos.student.selectTopic.TopicSelectionResponseDTO;
import com.uteq.sgtic.dtos.student.selectTopic.TopicSelectionStatusDTO;

public interface ITopicSelectionService {
    TopicSelectionResponseDTO procesarSeleccionOCambio(Integer idEstudiante, TopicSelectionRequestDTO request);
    TopicSelectionResponseDTO registrarPropuesta(Integer idEstudiante, Integer idPeriodo, Integer idOpcion, String titulo, String descripcion, String urlDocumento);

    TopicSelectionResponseDTO actualizarPropuesta(
            Integer idPropuesta, 
            Integer idEstudiante, 
            Integer idOpcion, 
            String titulo, 
            String descripcion, 
            String urlDocumento
    );

    TopicSelectionStatusDTO getEstado(Integer idEstudiante, Integer idPeriodo);
}