package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Data
@Entity
@Table(name = "docente", schema = "public")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_docente")
    private Integer idTeacher;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private User user;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;

    @ManyToMany
    @JoinTable(
            name = "docente_especializacion",
            joinColumns = @JoinColumn(name = "id_docente"),
            inverseJoinColumns = @JoinColumn(name = "id_especializacion")
    )

    private Set<Specialization> specializations;

    @ManyToMany
    @JoinTable(
            name = "docente_carrera",
            joinColumns = @JoinColumn(name = "id_docente"),
            inverseJoinColumns = @JoinColumn(name = "id_carrera")
    )
    private Set<Career> careers;
}
