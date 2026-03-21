package com.uteq.sgtic.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    @Column(name = "identificacion", nullable = false)
    private String identification;

    @Column(name = "nombres", nullable = false)
    private String firstName;

    @Column(name = "apellidos", nullable = false)
    private String lastName;

    @Column(name = "correo", nullable = false)
    private String email;

    // Mapeado como relación para mantener consistencia con Career y AcademicPeriod
    @ManyToOne
    @JoinColumn(name = "id_facultad", nullable = false)
    private Faculty faculty; 

    @Column(name = "fecha_envio", nullable = false)
    private LocalDate submissionDate;

    // Cambiado a Short para que coincida exactamente con el 'smallint' de PostgreSQL
    @Column(name = "nivel_solicitado", nullable = false)
    private Short requestedLevel;


    @Column(name = "estado", nullable = false)
    private String status;

    @Column(name = "observaciones")
    private String observations;

    @Column(name = "fecha_respuesta")
    private LocalDate responseDate;

    @ManyToOne
    @JoinColumn(name = "id_carrera", nullable = false)
    private Career career;

    @ManyToOne
    @JoinColumn(name = "id_periodo", nullable = false)
    private AcademicPeriod academicPeriod;

    //carlos
}