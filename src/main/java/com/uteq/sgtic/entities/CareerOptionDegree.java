package com.uteq.sgtic.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "carrera_opcion_titulacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerOptionDegree {
    @EmbeddedId
    private CareerOptionId id;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    public CareerOptionDegree(Integer idCarrera, Integer idOpcion) {
        this.id = new CareerOptionId(idCarrera, idOpcion);
        this.activo = true;
    }
}
