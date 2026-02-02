package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "asignacion_director")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class DirectorAssignment {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Integer idAssignment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_propuesta", unique = true)
    private WorkProposal proposal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_docente")
    private Teacher teacher;

    @Column(name = "decision", nullable = false, length = 15)
    private String decision;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;

    @Column(name = "fecha_decision", nullable = false)
    private LocalDate decisionDate;
}
