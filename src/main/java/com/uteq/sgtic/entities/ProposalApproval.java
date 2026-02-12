package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "aprobacion_propuesta")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProposalApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_propuesta", nullable = false)
    private WorkProposal workProposal;

    @Column(name = "entidad", nullable = false, length = 30)
    private String entity;

    @Column(name = "fecha", nullable = false)
    private LocalDate date;

    @Column(name = "aprobada", nullable = false)
    private Boolean approved;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;
}
