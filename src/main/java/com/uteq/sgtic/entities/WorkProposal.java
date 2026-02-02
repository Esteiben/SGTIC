package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "propuesta_trabajo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class WorkProposal {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_propuesta")
    private Integer idProposal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Student student;

    @Column(name = "titulo", nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "estado", nullable = false, length = 30)
    private String status;
}
