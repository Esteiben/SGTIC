package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.Teacher;
import com.uteq.sgtic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    @Query(value = "SELECT * FROM fn_docentes_reporte(CAST(:idCarrera AS INTEGER), CAST(:idPeriodo AS INTEGER))", nativeQuery = true)
    List<Object[]> callDocentesReporteFunction(@Param("idCarrera") Integer idCarrera,
                                               @Param("idPeriodo") Integer idPeriodo);
    // Usando el correo del token JWT
    Optional<Teacher> findByUserEmail(String email);

    String user(User user);
}
