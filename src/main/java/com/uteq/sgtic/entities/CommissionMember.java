package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "comision_miembro")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommissionMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_comision", nullable = false)
    private Commission commission;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_docente", nullable = false)
    private Teacher teacher;

    @Column(name = "rol", nullable = false, length = 20)
    private String role;
}
