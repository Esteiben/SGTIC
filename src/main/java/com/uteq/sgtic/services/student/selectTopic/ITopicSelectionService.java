package com.uteq.sgtic.services.student.selectTopic;

import com.uteq.sgtic.dtos.student.selectTopic.TopicSelectionRequestDTO;
import com.uteq.sgtic.dtos.student.selectTopic.TopicSelectionResponseDTO;

public interface ITopicSelectionService {
    TopicSelectionResponseDTO procesarSeleccionOCambio(Integer idEstudiante, TopicSelectionRequestDTO request);
    TopicSelectionResponseDTO registrarPropuesta(Integer idEstudiante, Integer idPeriodo, Integer idOpcion, String titulo, String descripcion, String urlDocumento);
}