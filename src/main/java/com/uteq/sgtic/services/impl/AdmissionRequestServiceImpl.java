package com.uteq.sgtic.services.impl;

import com.uteq.sgtic.entities.AdmissionRequest;
import com.uteq.sgtic.projections.RequestManagementCoordinatorProjection;
import com.uteq.sgtic.repository.SolicitudIngresoRepository;
import com.uteq.sgtic.services.IAdmissionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdmissionRequestServiceImpl extends CrudImpl<AdmissionRequest, Integer> implements IAdmissionRequestService {

    @Autowired
    private SolicitudIngresoRepository repository;

    @Override
    protected JpaRepository<AdmissionRequest, Integer> getRepository() {
        return repository;
    }

    @Override
    public List<RequestManagementCoordinatorProjection> listarParaCoordinador(Integer idCarrera) {
        return repository.listarParaCoordinador(idCarrera);
    }

    @Override
    public void aprobarSolicitud(Integer idSolicitud) {
        repository.aprobarSolicitud(idSolicitud);
    }

    @Override
    public void rechazarSolicitud(Integer idSolicitud, String motivo) {
        repository.rechazarSolicitud(idSolicitud, motivo);
    }
}