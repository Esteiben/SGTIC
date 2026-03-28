package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "aprobacion_tema_estudiante")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class StudentTopicApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_tema_propuesto", nullable = false)
    private StudentProposedTopic proposedTopic;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_comision", nullable = false)
    private Commission commission;

    @Column(name = "fecha", nullable = false)
    private LocalDate date;

    @Column(name = "aprobado", nullable = false)
    private Boolean approved;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;
}
