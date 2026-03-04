package com.uteq.sgtic.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatusDTO {
    private boolean temaSeleccionado;    // Para el círculo 1
    private boolean directorAsignado;     // Para el círculo 2
    private boolean procesoIniciado;      // Para el círculo 3
    private boolean tribunalAsignado;     // Para el círculo 4
    private boolean actaEntregada;        // Para el círculo 5
    private boolean finalizado;           // Para el círculo 6
    
    // Datos adicionales para las tarjetas
    private String nombreTema;
    private String nombreDirector;
}