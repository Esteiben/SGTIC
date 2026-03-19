package com.uteq.sgtic.dtos.teacherDTO;

import lombok.Data;

@Data
public class TeacherSaveDTO {
    private Integer idDocente;
    private String nombres;
    private String apellidos;
    private String cedula;
    private String correo;
    private String especialidades;
    private Integer idCarrera;
    private Integer idUsuarioLogueado;
}
