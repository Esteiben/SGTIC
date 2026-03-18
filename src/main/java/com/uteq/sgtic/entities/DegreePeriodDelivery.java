package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "entrega_periodo_titulacion")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class DegreePeriodDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estudiante_periodo_titulacion", nullable = false)
    private StudentDegreePeriod studentDegreePeriod;

    @Column(name = "numero_entrega")
    private Integer deliveryNumber;

    @Column(name = "tipo_entrega", length = 50)
    private String deliveryType;

    @Column(name = "fecha_entrega")
    private LocalDateTime deliveryDate;

    @Column(name = "archivo_url", length = 500)
    private String fileUrl;

    @Column(name = "estado_revision", length = 20)
    private String reviewStatus;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;
}   