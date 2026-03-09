package com.uteq.sgtic.repository.student;

import com.uteq.sgtic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDashboardRepository extends JpaRepository<User, Integer> {

    @Query(value = """
        SELECT
            d.prerequisitos_nivel1 AS "prerequisitosNivel1",
            d.tema_seleccionado AS "temaSeleccionado",
            d.director_asignado AS "directorAsignado",
            d.reuniones_minimas AS "reunionesMinimas",
            d.defensa_anteproyecto AS "defensaAnteproyecto",
            d.prerequisitos_nivel2 AS "prerequisitosNivel2",
            d.asistencia_tutorias AS "asistenciaTutorias",
            d.predefensa AS "predefensa",
            d.defensa_final AS "defensaFinal",
            d.nombre_tema AS "nombreTema",
            d.nombre_director AS "nombreDirector",
            d.nombre_opcion AS "nombreOpcion",
            d.total_tutorias AS "totalTutorias"
        FROM fn_estudiante_dashboard(:userId, :periodoId) d
        """, nativeQuery = true)
    DashboardProjection getStudentDashboardStatus(
            @Param("userId") Integer userId,
            @Param("periodoId") Integer periodoId
    );

    interface DashboardProjection {
        Boolean getPrerequisitosNivel1();
        Boolean getTemaSeleccionado();
        Boolean getDirectorAsignado();
        Boolean getReunionesMinimas();
        Boolean getDefensaAnteproyecto();
        Boolean getPrerequisitosNivel2();
        Boolean getAsistenciaTutorias();
        Boolean getPredefensa();
        Boolean getDefensaFinal();

        String getNombreTema();
        String getNombreDirector();
        String getNombreOpcion();

        Integer getTotalTutorias();
    }
}