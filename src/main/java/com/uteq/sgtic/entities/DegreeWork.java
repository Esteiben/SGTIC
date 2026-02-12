package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "trabajo_titulacion")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class DegreeWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_trabajo")
    private Integer idWork;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_estudiante", nullable = false, unique = true)
    private Student student;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_propuesta", nullable = false, unique = true)
    private WorkProposal workProposal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_director", nullable = false)
    private Teacher director;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_periodo", nullable = false)
    private AcademicPeriod academicPeriod;

    @Column(name = "estado", nullable = false, length = 30)
    private String status;
}
