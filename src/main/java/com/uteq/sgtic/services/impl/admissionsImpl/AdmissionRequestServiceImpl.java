package com.uteq.sgtic.services.impl.admissionsImpl;

import com.uteq.sgtic.entities.AdmissionRequest;

import com.uteq.sgtic.projections.admissions.RequestManagementCoordinatorProjection;
import com.uteq.sgtic.repository.admissions.AccessRequestRepository;
import com.uteq.sgtic.services.admissions.IAdmissionRequestService;
import com.uteq.sgtic.services.impl.CrudImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import com.uteq.sgtic.services.admissions.EmailService;
import java.util.Optional;

@Service
public class AdmissionRequestServiceImpl extends CrudImpl<AdmissionRequest, Integer> implements IAdmissionRequestService{

    @Autowired
    private AccessRequestRepository repository;

    @Autowired
    private EmailService emailService;

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
        Optional<AdmissionRequest> solicitudOpt = repository.findById(idSolicitud);
        repository.rechazarSolicitud(idSolicitud, motivo);

        if (solicitudOpt.isPresent()) {
            AdmissionRequest solicitud = solicitudOpt.get();
            String correoDestino = solicitud.getEmail();
            String nombreEstudiante = solicitud.getFirstName() + " " + solicitud.getLastName();
            emailService.enviarCorreoRechazo(correoDestino, nombreEstudiante, motivo);
        }
    }
}
