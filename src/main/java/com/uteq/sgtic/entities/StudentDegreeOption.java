package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "estudiante_opcion_titulacion")
@AllArgsConstructor
@NoArgsConstructor
public class StudentDegreeOption {

    @EmbeddedId
    private StudentDegreeOptionId id;

    @ManyToOne
    @MapsId("idStudent")
    @JoinColumn(name = "id_estudiante")
    private Student student;

    @ManyToOne
    @MapsId("idOption")
    @JoinColumn(name = "id_opcion")
    private DegreeOption degreeOption;

    @Column(name = "fecha_seleccion", nullable = false)
    private LocalDate selectionDate;
}
