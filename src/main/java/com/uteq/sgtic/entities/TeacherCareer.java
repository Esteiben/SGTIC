package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "docente_carrera")
@AllArgsConstructor
@NoArgsConstructor
public class TeacherCareer {

    @EmbeddedId
    private TeacherCareerId id;

    @ManyToOne
    @MapsId("idTeacher")
    @JoinColumn(name = "id_docente")
    private Teacher teacher;

    @ManyToOne
    @MapsId("idCareer")
    @JoinColumn(name = "id_carrera")
    private Career career;
}
