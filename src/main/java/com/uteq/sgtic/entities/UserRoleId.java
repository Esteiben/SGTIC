package com.uteq.sgtic.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleId implements Serializable {

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "id_facultad")
    private Integer idFacultad;

    @Column(name = "id_carrera")
    private Integer idCarrera;
}
