package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "opcion_titulacion")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class DegreeOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_opcion")
    private Integer idOption;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    @Column(name = "activo", nullable = false)
    private Boolean active;

    @Column(name = "icon_name", length = 50)
    private String iconName = "school";

    @Column(name = "fecha_creacion")
    private java.time.LocalDate createdAt = java.time.LocalDate.now();
}
