package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "usuario")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_usuario")
    private Integer idUser;

    @Column(name = "identificacion", nullable = false, length = 20)
    private String identification;

    @Column(name = "nombres", nullable = false, length = 100)
    private String firstName;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String lastName;

    @Column(name = "correo", nullable = false, length = 150)
    private String email;

    @Column(name = "activo", nullable = false)
    private Boolean active;
}
