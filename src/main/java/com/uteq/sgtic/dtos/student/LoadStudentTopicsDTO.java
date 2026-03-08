package com.uteq.sgtic.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoadStudentTopicsDTO {
    private Integer idTema;
    private String titulo;
    private String descripcion;
    private Integer idCarrera;
    private Integer idOpcion;
    private String nombreOpcion;
}


