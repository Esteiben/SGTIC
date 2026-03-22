package com.uteq.sgtic.dtos.student.selectTopic;

import lombok.Data;

@Data
public class TopicSelectionStatusDTO {
    private String estadoTitulacion;
    private String tipoTemaActual;
    private Boolean fueraDePlazo;
    private Boolean desactivadoPorPlazo;
    private Boolean tieneProcesoTema;
    private Boolean tieneSeleccionBanco;
    private Boolean puedeSeleccionar;
    private Boolean puedeProponer;
    private Boolean puedeCambiarTema;
    private Integer cambiosTemaRealizados;
    private String fechaLimiteSeleccion;
    private String mensaje;
}