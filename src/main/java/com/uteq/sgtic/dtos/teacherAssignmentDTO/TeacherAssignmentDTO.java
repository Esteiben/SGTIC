package com.uteq.sgtic.dtos.teacherAssignmentDTO;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
public class TeacherAssignmentDTO {
    private Integer idDocente;
    private String nombreCompleto;
    private List<String> especialidades;
    private Integer proyectosActivos;
    private Double matchIA;
    private String fotoUrl;
}
