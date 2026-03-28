package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleId implements Serializable {

    @Column(name = "id_usuario")
    private Integer idUser;

    @Column(name = "id_rol")
    private Integer idRole;
}
