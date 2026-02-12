package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tutoria_trabajo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class WorkTutoring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_tutoria")
    private Integer idTutoring;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_trabajo", nullable = false)
    private DegreeWork degreeWork;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_director", nullable = false)
    private Teacher director;

    @Column(name = "fecha", nullable = false)
    private LocalDate date;

    @Column(name = "tipo", nullable = false, length = 30)
    private String type;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;

    @Column(name = "registrada", nullable = false)
    private Boolean registered;
}
