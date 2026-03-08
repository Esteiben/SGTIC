package com.uteq.sgtic.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveTopicSelectionRequestDTO {
    private Integer idTema;
    private Integer idOpcion;
    private Integer idPeriodo;
}