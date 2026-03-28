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
    @JoinColumn(name = "id_estudiante_periodo_titulacion", nullable = false)
    private StudentDegreePeriod enrollment;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_tema", nullable = true)
    private Topic topic;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_tema_propuesto", nullable = true)
    private StudentProposedTopic proposedTopic;

    // Campo de período eliminado/comentado según indicación
    // @ManyToOne(optional = false)
    // @JoinColumn(name = "id_periodo", nullable = false)
    // private AcademicPeriod academicPeriod;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDate sentDate;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;
}