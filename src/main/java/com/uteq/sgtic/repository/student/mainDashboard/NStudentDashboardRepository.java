package com.uteq.sgtic.repository.student.mainDashboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uteq.sgtic.entities.User;

@Repository
public interface NStudentDashboardRepository extends JpaRepository<User, Integer> {

    // Llama directamente a la función de PostgreSQL
    @Query(value = "SELECT * FROM fn_estudiante_dashboard(:estudianteId, :periodoId)", nativeQuery = true)
    DashboardProjection getStudentDashboardStatus(@Param("estudianteId") Integer estudianteId, @Param("periodoId") Integer periodoId);

    // Mapea exactamente las columnas que devuelve tu función de pgAdmin
    interface DashboardProjection {
        Boolean getEsta_matriculado();
        Boolean getPrerequisitos_nivel1();
        Boolean getTema_seleccionado();
        Boolean getDirector_asignado();
        Boolean getReuniones_minimas();
        Boolean getDefensa_anteproyecto();
        Boolean getPrerequisitos_nivel2();
        Boolean getAsistencia_tutorias();
        Boolean getPredefensa();
        Boolean getDefensa_final();
        String getNombre_tema();
        String getNombre_director();
        String getNombre_opcion();
        Integer getTotal_tutorias();
    }

    //segundo comentario de prueba borrar
}