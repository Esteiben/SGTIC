package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tribunal")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Tribunal {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tribunal")
    private Integer idTribunal;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trabajo", unique = true)
    private GraduationWork trabajo;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate assignedDate;
}
