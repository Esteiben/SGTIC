package com.uteq.sgtic.dtos.student.selectTopic;

import lombok.Data;

@Data
public class TemaDTO {
    private Integer idTema;
    private String titulo;
    private String descripcion;
    private Integer idOpcion;
    private String nombreOpcion;
    private String profesor;
    private String area;
    private String duracion;
}