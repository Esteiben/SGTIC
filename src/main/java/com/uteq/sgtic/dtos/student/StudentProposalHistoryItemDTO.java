package com.uteq.sgtic.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProposalHistoryItemDTO {
    private Integer numeroVersion;
    private boolean esVersionActual;
    private String titulo;
    private String descripcion;
    private String urlDocumento;
    private Integer idOpcion;
    private String nombreOpcion;
    private String estadoPropuesta;
    private String estadoTema;
    private String feedbackDocente;
    private LocalDate fechaEnvio;
    private LocalDateTime fechaMovimiento;
    private String motivo;
}