package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tema_propuesto_estudiante")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class StudentProposedTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_tema_propuesto")
    private Integer idProposedTopic;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Student student;

    @Column(name = "titulo", nullable = false, length = 300)
    private String title;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "fecha_propuesta", nullable = false)
    private LocalDate proposalDate;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;
}
