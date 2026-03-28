package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "usuario_rol")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @MapsId("idUser")
    @JoinColumn(name = "id_usuario")
    private User user;

    @ManyToOne
    @MapsId("idRole")
    @JoinColumn(name = "id_rol")
    private Role role;
}
