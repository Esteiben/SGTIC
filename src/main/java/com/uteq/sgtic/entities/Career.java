package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "carrera")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_carrera")
    private Integer idCareer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_facultad", nullable = false)
    private Faculty faculty;

    @Column(name = "nombre", nullable = false, length = 150)
    private String name;

    @Column(name = "activa")
    private Boolean active;
}
