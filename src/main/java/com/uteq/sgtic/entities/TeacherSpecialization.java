package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "docente_especializacion")
@AllArgsConstructor
@NoArgsConstructor
public class TeacherSpecialization {

    @EmbeddedId
    private TeacherSpecializationId id;

    @ManyToOne
    @MapsId("idTeacher")
    @JoinColumn(name = "id_docente")
    private Teacher teacher;

    @ManyToOne
    @MapsId("idSpecialization")
    @JoinColumn(name = "id_especializacion")
    private Specialization specialization;
}
