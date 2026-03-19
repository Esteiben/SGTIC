package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.Teacher;
import com.uteq.sgtic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query(value = "SELECT * FROM fn_listar_docentes(CAST(:idUsuario AS INTEGER))", nativeQuery = true)
    List<Object[]> callListarDocentesPorCoordinador(@Param("idUsuario") Integer idUsuario);

    @Modifying
    @Transactional
    @Query(value = "CALL sp_actualizar_estado_docente(:idDocente, :nuevoEstado)", nativeQuery = true)
    void callActualizarEstado(@Param("idDocente") Integer idTeacher, @Param("nuevoEstado") String status);

    @Modifying
    @Transactional
    @Query(value = "CALL sp_gestion_docente(:idDocente, :nombre, :apellido, :cedula, :correo, :especialidades, :idCarrera, :idUsuarioLogueado)", nativeQuery = true)
    void callGestionDocente(
            @Param("idDocente") Integer id,
            @Param("nombre") String nombre,
            @Param("apellido") String apellido,
            @Param("cedula") String cedula,
            @Param("correo") String correo,
            @Param("especialidades") String especialidades,
            @Param("idCarrera") Integer idCarrera,
            @Param("idUsuarioLogueado") Integer idUsuarioLogueado // <-- NUEVO
    );

    @Query(value = "SELECT * FROM fn_docentes_reporte(CAST(:idCarrera AS INTEGER), CAST(:idPeriodo AS INTEGER))", nativeQuery = true)
    List<Object[]> callDocentesReporteFunction(@Param("idCarrera") Integer idCarrera,
                                               @Param("idPeriodo") Integer idPeriodo);

    Optional<Teacher> findByUserEmail(String email);
    String user(User user);
}
