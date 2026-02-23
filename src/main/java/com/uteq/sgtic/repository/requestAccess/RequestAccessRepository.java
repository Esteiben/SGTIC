package com.uteq.sgtic.repository.requestAccess;

import com.uteq.sgtic.entities.AdmissionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestAccessRepository extends JpaRepository<AdmissionRequest, Integer> {
    
    @Procedure(procedureName = "request_access")
    void requestAccess(
            @Param("p_identificacion") String idIdentificacion,
            @Param("p_correo") String correo,
            @Param("p_nombres") String nombres,
            @Param("p_apellidos") String apellidos,
            @Param("p_id_faculty") Integer idFacultad,
            @Param("p_id_career") Integer idCarrera
    );
}