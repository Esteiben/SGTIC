package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "estudiante_periodo_titulacion")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class StudentDegreePeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_periodo", nullable = false)
    private AcademicPeriod academicPeriod;

    @Column(name = "tipo_periodo", length = 50)
    private String periodType; // ej: DESARROLLO_I, DESARROLLO_II

    @Column(name = "aprobado")
    private Boolean approved;

    @Column(name = "fecha_aprobacion")
    private LocalDateTime approvalDate;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;

    @Column(name = "nivel", nullable = false)
    private Integer level;

    @Column(name = "tipo_matricula", length = 50)
    private String enrollmentType; // INICIAL, REMATRICULA

    @Column(name = "estado_periodo", nullable = false, length = 20)
    private String periodStatus; // EN_CURSO, APROBADO, REPROBADO, ANULADO

    @Column(name = "fecha_matricula", nullable = false)
    private LocalDateTime enrollmentDate;

    @Column(name = "fecha_cierre")
    private LocalDateTime closingDate;

    @ManyToOne
    @JoinColumn(name = "id_solicitud_ingreso")
    private AdmissionRequest admissionRequest;

    @ManyToOne
    @JoinColumn(name = "id_registro_anterior")
    private StudentDegreePeriod previousRecord;
}