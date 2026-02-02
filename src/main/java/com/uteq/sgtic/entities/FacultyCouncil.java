package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "consejo_facultad")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class FacultyCouncil {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consejo")
    private Integer idCouncil;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_facultad", unique = true)
    private Faculty faculty;

    @Column(name = "activo", nullable = false)
    private Boolean active;
}
