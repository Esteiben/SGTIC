package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "facultad")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Faculty {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_facultad")
    private Integer idFaculty;

    @Column(name = "nombre", nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "siglas", nullable = false, unique = true, length = 10)
    private String acronym;
}
