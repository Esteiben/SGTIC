package com.uteq.sgtic.repository.requestAccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uteq.sgtic.entities.AdmissionRequest;

@Repository
public interface RequestAccessRepository extends JpaRepository<AdmissionRequest, Integer> {

    @Query(value = "SELECT * FROM fn_en_enviar_solicitud_acceso(:identificacion, :nombres, :apellidos, :correo, :idCarrera, :idFacultad)", nativeQuery = true)
    RequestResultProjection enviarSolicitud(
            @Param("identificacion") String identificacion,
            @Param("nombres") String nombres,
            @Param("apellidos") String apellidos,
            @Param("correo") String correo,
            @Param("idCarrera") Integer idCarrera,
            @Param("idFacultad") Integer idFacultad
    );

    interface RequestResultProjection {
        Integer getId_solicitud(); // Mapea id_solicitud
        Boolean getExito();        // Mapea exito
        String getMensaje();       // Mapea mensaje
    }
}