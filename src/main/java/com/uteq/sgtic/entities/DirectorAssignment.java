package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "asignacion_director")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class DirectorAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_propuesta", nullable = false, unique = true)
    private WorkProposal workProposal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_docente", nullable = false)
    private Teacher teacher;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate assignmentDate;

    @Column(name = "respuesta", nullable = false, length = 20)
    private String response;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;
}
