package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "acta_grado")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class DegreeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_acta")
    private Integer idRecord;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_estudiante", nullable = false, unique = true)
    private Student student;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_trabajo", nullable = false, unique = true)
    private DegreeWork degreeWork;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate issueDate;

    @Column(name = "resultado", nullable = false, length = 20)
    private String result;

    @Column(name = "nota_final", nullable = false, precision = 4, scale = 2)
    private BigDecimal finalGrade;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;
}
