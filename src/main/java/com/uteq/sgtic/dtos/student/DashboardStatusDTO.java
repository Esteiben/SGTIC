package com.uteq.sgtic.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatusDTO {

    private boolean prerequisitosNivel1;
    private boolean temaSeleccionado;
    private boolean directorAsignado;
    private boolean reunionesMinimas;
    private boolean defensaAnteproyecto;
    private boolean prerequisitosNivel2;
    private boolean asistenciaTutorias;
    private boolean predefensa;
    private boolean defensaFinal;

    private String nombreTema;
    private String nombreDirector;
    private String nombreOpcion;

    private Integer totalTutorias;
}