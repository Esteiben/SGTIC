package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "solicitud_ingreso")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdmissionRequest {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera", nullable = false)
    private Career career;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDate sentDate;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;
}
