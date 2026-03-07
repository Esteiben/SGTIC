package com.uteq.sgtic.repository.General;

import com.uteq.sgtic.entities.DegreeOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Collections;

@Repository
public interface DegreeOptionRepository extends JpaRepository<DegreeOption, Integer>{
    @Query(value = "SELECT * FROM v_opcion_titulacion_activa_por_carrera WHERE id_Carrera = ?1", nativeQuery = true)
    List<DegreeOption> findActiveByCareerId(Integer idCarrera);


    // Método falso temporal que devuelve lista vacía
    default List<DegreeOption> EstoNoVale() {
        return Collections.emptyList();
    }
}
