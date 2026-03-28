package com.uteq.sgtic.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerOptionId implements Serializable {
    @Column(name = "id_carrera")
    private Integer idCarrera;

    @Column(name = "id_opcion")
    private Integer idOpcion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CareerOptionId that = (CareerOptionId) o;
        return Objects.equals(idCarrera, that.idCarrera) &&
                Objects.equals(idOpcion, that.idOpcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCarrera, idOpcion);
    }
}
