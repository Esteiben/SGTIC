package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class TeacherCareerId implements Serializable {

    @Column(name = "id_docente")
    private Integer idTeacher;

    @Column(name = "id_carrera")
    private Integer idCareer;
}
