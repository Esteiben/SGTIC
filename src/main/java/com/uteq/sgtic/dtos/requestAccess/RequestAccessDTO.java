package com.uteq.sgtic.dtos.requestAccess;

import lombok.Data;

@Data

public class RequestAccessDTO {
    private String identificacion;
    private String nombres;
    private String apellidos;
    private String correo;
    private Integer idFacultad;
    private Integer idCarrera;
}
