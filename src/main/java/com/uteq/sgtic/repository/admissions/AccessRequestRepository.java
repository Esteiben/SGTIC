package com.uteq.sgtic.repository.admissions;

import com.uteq.sgtic.projections.admissions.PendingAlertProjection;
import com.uteq.sgtic.projections.admissions.RequestManagementCoordinatorProjection;
import com.uteq.sgtic.entities.AdmissionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AccessRequestRepository extends JpaRepository<AdmissionRequest, Integer> {

    long countByStatus(String status);

    @Query(value = "SELECT * FROM public.fn_listar_solicitudes(:idUsuario)", nativeQuery = true)
    List<RequestManagementCoordinatorProjection> listarSolicitudesPorCarrera(@Param("idUsuario") Integer idUsuario);

    @Modifying
    @Transactional
    @Query(value = "CALL public.sp_aprobar_solicitud(:idRequest, :claveTemp)", nativeQuery = true)
    void aprobarSolicitud(@Param("idRequest") Integer idRequest, @Param("claveTemp") String claveTemp);

    @Modifying
    @Transactional
    @Query(value = "CALL public.sp_rechazar_solicitud(:idRequest, :motivo)", nativeQuery = true)
    void rechazarSolicitud(@Param("idRequest") Integer idRequest, @Param("motivo") String motivo);

    @Query(value = "SELECT * FROM public.fn_buscar_solicitudes_atrasadas()", nativeQuery = true)
    List<PendingAlertProjection> buscarSolicitudesAtrasadas();
}
