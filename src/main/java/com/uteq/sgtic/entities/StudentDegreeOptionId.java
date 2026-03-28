package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class StudentDegreeOptionId implements Serializable {

    @Column(name = "id_estudiante")
    private Integer idStudent;

    @Column(name = "id_opcion")
    private Integer idOption;
}
