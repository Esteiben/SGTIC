package com.uteq.sgtic.repository.General;

import com.uteq.sgtic.entities.DegreeOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DegreeOptionRepository extends JpaRepository<DegreeOption, Integer> {

    interface DegreeOptionProjection {
        Integer getIdOption();
        String getNombre();
        String getDescripcion();
    }

    @Query(value = """
        SELECT
            id_opcion AS idOption,
            nombre,
            descripcion
        FROM v_opcion_titulacion_activa_por_carrera
        WHERE id_carrera = ?1
        ORDER BY nombre
        """, nativeQuery = true)
    List<DegreeOptionProjection> findActiveByCareerId(Integer idCarrera);
}