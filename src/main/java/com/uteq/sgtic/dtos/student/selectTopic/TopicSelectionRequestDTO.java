package com.uteq.sgtic.dtos.student.selectTopic;

import lombok.Data;

@Data
public class TopicSelectionRequestDTO {
    private Integer idPeriodo;  //matricula
    private Integer idOpcion;
    private Integer idTema;
    private Integer idTemaPropuesto;
    private String motivo;
}