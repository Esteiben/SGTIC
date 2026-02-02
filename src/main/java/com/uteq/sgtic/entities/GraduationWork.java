package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "trabajo_titulacion")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class GraduationWork {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trabajo")
    private Integer idWork;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_propuesta", unique = true)
    private WorkProposal proposal;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;
}
