package com.uteq.sgtic.dtos.teacherDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO {
    private Integer idDocente;
    private String nombreCompleto;
    private String correo;
    private List<String> especialidades;
    private String estado;
    private boolean activo;
    private String cedula;
    private String nombres;
    private String apellidos;
}
