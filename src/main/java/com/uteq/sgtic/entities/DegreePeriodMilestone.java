package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "hito_periodo_titulacion")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class DegreePeriodMilestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estudiante_periodo_titulacion", nullable = false)
    private StudentDegreePeriod studentDegreePeriod;

    @Column(name = "tipo_hito", length = 50)
    private String milestoneType; // ej: ANTEPROYECTO, PREDEFENSA, DEFENSA

    @Column(name = "estado", length = 20)
    private String status;

    @Column(name = "fecha")
    private LocalDateTime date;

    @Column(name = "nota", precision = 4, scale = 2)
    private BigDecimal grade;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;
}