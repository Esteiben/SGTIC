package com.uteq.sgtic.services.admissions;

import com.uteq.sgtic.entities.AdmissionRequest;
import com.uteq.sgtic.projections.admissions.RequestManagementCoordinatorProjection;
import com.uteq.sgtic.services.ICrud;
import java.util.List;

public interface IAdmissionRequestService extends ICrud<AdmissionRequest, Integer> {
    List<RequestManagementCoordinatorProjection> listarParaCoordinador(Integer idCarrera);
    void aprobarSolicitud(Integer idSolicitud);
    void rechazarSolicitud(Integer idSolicitud, String motivo);
}
