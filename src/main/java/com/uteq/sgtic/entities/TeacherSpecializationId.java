package com.uteq.sgtic.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class TeacherSpecializationId implements Serializable {

    @Column(name = "id_docente")
    private Integer idDocente;

    @Column(name = "id_especializacion")
    private Integer idEspecializacion;
}
