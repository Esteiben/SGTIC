package com.uteq.sgtic.repository.teacherAssignmentRepository;

import com.uteq.sgtic.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TeacherAssignmentRepository extends JpaRepository<Teacher, Integer> {

    @Query(value = "SELECT * FROM fn_docentes_para_asignacion(:idUsuario)", nativeQuery = true)
    List<Object[]> findDocentesByCoordinador(@Param("idUsuario") Integer idUsuario);

    @Modifying
    @Transactional
    @Query(value = "CALL sp_asignar_director_proyecto(:idPropuesta, :idDocente, :idPeriodo)", nativeQuery = true)
    void asignarDirector(@Param("idPropuesta") Integer idPropuesta,
                         @Param("idDocente") Integer idDocente,
                         @Param("idPeriodo") Integer idPeriodo);

    @Query(value = "SELECT * FROM fn_proyectos_pendientes_coordinador(:idUsuario)", nativeQuery = true)
    List<Object[]> findProyectosPendientes(@Param("idUsuario") Integer idUsuario);

    @Query(value = "SELECT * FROM fn_get_datos_notificacion_asignacion(:idPropuesta, :idDocente)", nativeQuery = true)
    List<Object[]> findDatosParaNotificacion(@Param("idPropuesta") Integer idPropuesta,
                                             @Param("idDocente") Integer idDocente);

}