package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "propuesta_trabajo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class WorkProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_propuesta")
    private Integer idProposal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_tema", nullable = false)
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "id_tema_propuesto")
    private StudentProposedTopic proposedTopic;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_periodo", nullable = false)
    private AcademicPeriod academicPeriod;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDate sentDate;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;
}
