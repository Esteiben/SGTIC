package com.uteq.sgtic.repository.banckTemaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.uteq.sgtic.entities.BanckTema;

import java.util.List;

@Repository
public interface BanckTemaRepository extends JpaRepository<BanckTema, Integer>{

    @Query(value = "SELECT * FROM fn_obtener_temas_por_coordinador(:idUsuario)", nativeQuery = true)
    List<Object[]> findTemasActivosRaw(@Param("idUsuario") Integer idUsuario);

    @Modifying
    @Transactional
    @Query(value = "CALL sp_insertar_tema_con_comision(:idCarrera, :idOpcion, :titulo, :descripcion, :nombreComision)", nativeQuery = true)
    void guardarTemaConComision(
            @Param("idCarrera") Integer idCarrera,
            @Param("idOpcion") Integer idOpcion,
            @Param("titulo") String titulo,
            @Param("descripcion") String descripcion,
            @Param("nombreComision") String nombreComision
    );

    @Modifying
    @Transactional
    @Query(value = "CALL sp_editar_tema_coordinador(:idTema, :idOpcion, :titulo, :descripcion, :nombreComision)", nativeQuery = true)
    void editarTema(
            @Param("idTema") Integer idTema,
            @Param("idOpcion") Integer idOpcion,
            @Param("titulo") String titulo,
            @Param("descripcion") String descripcion,
            @Param("nombreComision") String nombreComision
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE tema SET activo = false WHERE id_tema = :idTema", nativeQuery = true)
    void eliminar(@Param("idTema") Integer idTema);
}
