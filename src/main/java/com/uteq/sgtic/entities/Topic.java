package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tema")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_tema")
    private Integer idTopic;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_carrera", nullable = false)
    private Career career;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_comision", nullable = false)
    private Commission commission;

    @Column(name = "titulo", nullable = false, length = 300)
    private String title;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "activo", nullable = false)
    private Boolean active;

    @Column(name = "es_banco", nullable = false)
    private Boolean bank;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate creationDate;
}
