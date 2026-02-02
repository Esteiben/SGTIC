package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "estudiante")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private Integer idStudent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera", nullable = false)
    private Career career;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate entryDate;
}
