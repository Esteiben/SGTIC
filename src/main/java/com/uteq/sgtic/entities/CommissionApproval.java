package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "aprobacion_comision")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommissionApproval {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aprobacion")
    private Integer idApproval;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_propuesta", unique = true)
    private WorkProposal proposal;

    @Column(name = "aprobada", nullable = false)
    private Boolean approved;

    @Column(name = "fecha", nullable = false)
    private LocalDate date;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;
}
