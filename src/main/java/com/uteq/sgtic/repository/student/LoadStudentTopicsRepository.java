package com.uteq.sgtic.repository.student;

import com.uteq.sgtic.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoadStudentTopicsRepository extends JpaRepository<Topic, Integer> {

    interface TemaDisponibleProjection {
        Integer getIdTema();
        String getTitulo();
        String getDescripcion();
        Integer getIdCarrera();
        Integer getIdOpcion();
        String getNombreOpcion();
    }

    @Query(value = """
        SELECT
            id_tema AS idTema,
            titulo AS titulo,
            descripcion AS descripcion,
            id_carrera AS idCarrera,
            id_opcion AS idOpcion,
            nombre_opcion AS nombreOpcion
        FROM vista_temas_disponibles
        WHERE id_carrera = :idCarrera
          AND id_opcion = :idOpcion
        ORDER BY titulo
        """, nativeQuery = true)
    List<TemaDisponibleProjection> findTemasDisponibles(
            @Param("idCarrera") Integer idCarrera,
            @Param("idOpcion") Integer idOpcion
    );
}