package com.uteq.sgtic.services;
import com.uteq.sgtic.entities.AdmissionRequest;
import com.uteq.sgtic.projections.RequestManagementCoordinatorProjection;
import java.util.List;

public interface IAdmissionRequestService extends ICrud<AdmissionRequest, Integer> {
    List<RequestManagementCoordinatorProjection> listarParaCoordinador(Integer idCarrera);
    void aprobarSolicitud(Integer idSolicitud);
    void rechazarSolicitud(Integer idSolicitud, String motivo);
}