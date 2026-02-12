package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "periodo_academico")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class AcademicPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_periodo")
    private Integer idPeriod;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate startDate;

    @Column(name = "fecha_fin", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
    private LocalDate endDate;

    @Column(name = "activo", nullable = false)
    private Boolean active;
}
