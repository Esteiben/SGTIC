package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.Student;
import com.uteq.sgtic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByUserIdentification(String identification);

    Optional<Student> findByUserId(Integer userId);
    // Buscar estudiante por objeto User
    Optional<Student> findByUser(User user);

    @Query(value = "SELECT DISTINCT estado FROM propuesta_trabajo WHERE estado IS NOT NULL", nativeQuery = true)
    List<String> findDistinctEstados();

    @Query(value = "SELECT * FROM fn_estadisticas_reporte(CAST(:idCarrera AS INTEGER), CAST(:idPeriodo AS INTEGER))", nativeQuery = true)
    List<Object[]> callEstadisticasReporteFunction(@Param("idCarrera") Integer idCarrera,
                                                   @Param("idPeriodo") Integer idPeriodo);

    @Query(value = "SELECT * FROM fn_estudiantes_reporte(CAST(:idCarrera AS INTEGER), CAST(:idPeriodo AS INTEGER), CAST(:estado AS VARCHAR), CAST(:idDirector AS INTEGER))", nativeQuery = true)
    List<Object[]> callEstudiantesReporteFunction(@Param("idCarrera") Integer idCarrera,
                                                  @Param("idPeriodo") Integer idPeriodo,
                                                  @Param("estado") String estado,
                                                  @Param("idDirector") Integer idDirector);

}


