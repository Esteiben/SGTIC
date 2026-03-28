package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class TeacherSpecializationId implements Serializable {

    @Column(name = "id_docente")
    private Integer idTeacher;

    @Column(name = "id_especializacion")
    private Integer idSpecialization;
}
