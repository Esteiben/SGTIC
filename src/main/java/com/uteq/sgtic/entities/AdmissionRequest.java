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

    @Column(name = "identificacion")
    private String identification;

    @Column(name = "nombres")
    private String firstName;

    @Column(name = "apellidos")
    private String lastName;

    @Column(name = "correo")
    private String email;

    @Column(name = "estado")
    private String status;

    @Column(name = "observaciones")
    private String observations;

    @Column(name = "fecha_respuesta")
    private LocalDate responseDate;

    @ManyToOne
    @JoinColumn(name = "id_carrera")
    private Career career;

    @ManyToOne
    @JoinColumn(name = "id_periodo")
    private AcademicPeriod academicPeriod;
}
