package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "docente_especializacion")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class TeacherSpecialization {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private TeacherSpecializationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idDocente")
    @JoinColumn(name = "id_docente")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idEspecializacion")
    @JoinColumn(name = "id_especializacion")
    private Specialization specialization;
}
