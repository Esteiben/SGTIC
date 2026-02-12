package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tribunal")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Tribunal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_tribunal")
    private Integer idTribunal;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_trabajo", nullable = false, unique = true)
    private DegreeWork degreeWork;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_periodo", nullable = false)
    private AcademicPeriod academicPeriod;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate assignmentDate;
}
