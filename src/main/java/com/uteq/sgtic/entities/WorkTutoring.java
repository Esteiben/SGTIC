package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tutoria_trabajo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class WorkTutoring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_tutoria")
    private Integer idTutoring;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_trabajo", nullable = false)
    private DegreeWork degreeWork;

    @Column(name = "fecha", nullable = false)
    private LocalDate date;

    @Column(name = "tipo", nullable = false, length = 30)
    private String type;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;

    @Column(name = "registrada", nullable = false)
    private Boolean registered;

    @Column(name = "modalidad", length = 20)
    private String modality;

    @Column(name = "lugar_enlace", length = 255)
    private String locationLink;

    @Column(name = "asistencia")
    private Boolean attendance;

    @Column(name = "url_informe", length = 500)
    private String reportUrl;
}
