package com.uteq.sgtic.repository.optionDegreeCareer;

import com.uteq.sgtic.entities.CareerOptionDegree;
import com.uteq.sgtic.entities.CareerOptionId;
import com.uteq.sgtic.projections.optionCareer.OptionCareerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionDegreeCareerRepository extends JpaRepository<CareerOptionDegree,CareerOptionId>{
    @Query(value = "SELECT * FROM fn_obtener_opciones_titulacion(:idUsuario)", nativeQuery = true)
    List<OptionCareerProjection> getOptionsByCareer(@Param("idUsuario") Integer idUsuario);

    @Modifying
    @Query(value = "CALL sp_opcion_carrera_titulacion(:idUsuario, :idOpcion, :seleccionado)", nativeQuery = true)
    void toggleOptionCareer(
            @Param("idUsuario") Integer idUsuario,
            @Param("idOpcion") Integer idOpcion,
            @Param("seleccionado") Boolean seleccionado
    );
}
