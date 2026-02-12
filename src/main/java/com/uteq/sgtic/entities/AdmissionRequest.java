package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "solicitud_ingreso")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdmissionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_solicitud")
    private Integer idRequest;

    @Column(name = "identificacion", nullable = false, length = 20)
    private String identification;

    @Column(name = "nombres", nullable = false, length = 100)
    private String firstName;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String lastName;

    @Column(name = "correo", nullable = false, length = 150)
    private String email;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_carrera", nullable = false)
    private Career career;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_periodo", nullable = false)
    private AcademicPeriod academicPeriod;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDate sentDate;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;
}
