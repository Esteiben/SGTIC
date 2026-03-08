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
            (s.id_tema IS NOT NULL) as temaSeleccionado,
            (s.id_director IS NOT NULL) as directorAsignado,
            (s.estado_proceso = 'EN_CURSO') as procesoIniciado,
            (s.id_tribunal IS NOT NULL) as tribunalAsignado,
            (s.fecha_entrega_acta IS NOT NULL) as actaEntregada,
            (s.estado_proceso = 'FINALIZADO') as finalizado,
            t.nombre as nombreTema,
            d.nombre_completo as nombreDirector
        FROM estudiante s
        LEFT JOIN tema t ON s.id_tema = t.id
        LEFT JOIN docente d ON s.id_director = d.id
        WHERE s.id_usuario = :userId
        """, nativeQuery = true)
    DashboardProjection getStudentDashboardStatus(@Param("userId") Integer userId);

    interface DashboardProjection {
        Boolean getTemaSeleccionado();
        Boolean getDirectorAsignado();
        Boolean getProcesoIniciado();
        Boolean getTribunalAsignado();
        Boolean getActaEntregada();
        Boolean getFinalizado();
        String getNombreTema();
        String getNombreDirector();
    }
}