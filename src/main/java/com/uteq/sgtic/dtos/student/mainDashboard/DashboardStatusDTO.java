package com.uteq.sgtic.dtos.student.mainDashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatusDTO {
    private boolean estaMatriculado;
    
    // Nivel 1
    private boolean prerequisitosNivel1;
    private boolean temaSeleccionado;
    private boolean directorAsignado;
    private boolean defensaAnteproyecto;

    // Nivel 2
    private boolean prerequisitosNivel2;
    private boolean asistenciaTutorias;
    private boolean predefensa;
    private boolean defensaFinal;

    // Info General
    private String nombreTema;
    private String nombreDirector;
    private String nombreOpcion;
    private Integer totalTutorias;
    private Integer minimoTutorias;
}