package com.uteq.sgtic.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveTopicSelectionResponseDTO {
    private String message;
    private Integer idTema;
    private Integer idOpcion;
    private Integer idPeriodo;
    private LocalDate fechaLimiteSeleccion;
}