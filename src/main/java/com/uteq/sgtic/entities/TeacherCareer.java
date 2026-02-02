package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "docente_carrera")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class TeacherCareer {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private TeacherCareerId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idDocente")
    @JoinColumn(name = "id_docente")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCarrera")
    @JoinColumn(name = "id_carrera")
    private Career career;
}
