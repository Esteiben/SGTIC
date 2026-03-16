package com.uteq.sgtic.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicSelectionStatusDTO {
    private String estadoTitulacion;
    private String tipoTemaActual; // NINGUNO | BANCO | PROPUESTO
    private boolean fueraDePlazo;
    private boolean desactivadoPorPlazo;
    private boolean tieneProcesoTema;
    private boolean tieneSeleccionBanco;
    private boolean puedeSeleccionar;
    private boolean puedeProponer;
    private boolean puedeCambiarTema;
    private Integer cambiosTemaRealizados;
    private LocalDate fechaLimiteSeleccion;
    private String mensaje;
}