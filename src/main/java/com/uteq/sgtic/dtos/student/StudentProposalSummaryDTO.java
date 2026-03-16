package com.uteq.sgtic.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProposalSummaryDTO {
    private Integer idPropuesta;
    private Integer idTemaPropuesto;
    private String titulo;
    private String descripcion;
    private String estadoPropuesta;
    private String estadoTema;
    private String feedbackDocente;
    private String urlDocumento;
    private Integer idOpcion;
    private String nombreOpcion;
    private Integer numeroVersion;
    private Integer totalVersiones;
    private LocalDate fechaEnvio;
    private LocalDateTime fechaUltimaActualizacion;
    private boolean editable;
}