package com.uteq.sgtic.services.impl.admissionsImpl;

import com.uteq.sgtic.entities.AdmissionRequest;
import com.uteq.sgtic.projections.admissions.RequestManagementCoordinatorProjection;
import com.uteq.sgtic.repository.admissions.AccessRequestRepository;
import com.uteq.sgtic.services.admissions.IAdmissionRequestService;
import com.uteq.sgtic.services.admissions.EmailService;
import com.uteq.sgtic.services.impl.CrudImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;

@Service
public class AdmissionRequestServiceImpl extends CrudImpl<AdmissionRequest, Integer> implements IAdmissionRequestService {

    @Autowired
    private AccessRequestRepository repository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected JpaRepository<AdmissionRequest, Integer> getRepository() {
        return repository;
    }

    @Override
    public List<RequestManagementCoordinatorProjection> listarParaCoordinador(Integer idUsuario) {
        return repository.listarSolicitudesPorCarrera(idUsuario);
    }

    @Override
    @Transactional
    public void aprobarSolicitud(Integer idRequest) {
        AdmissionRequest solicitud = repository.findById(idRequest)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        String passwordTemporal = generarPasswordAleatorio();
        String passwordEncriptado = passwordEncoder.encode(passwordTemporal);
        repository.aprobarSolicitud(idRequest, passwordEncriptado);

        emailService.enviarCredenciales(
                solicitud.getEmail(),
                solicitud.getFirstName(),
                solicitud.getEmail(),
                passwordTemporal
        );
    }

    @Override
    @Transactional
    public void rechazarSolicitud(Integer idRequest, String motivo) {
        AdmissionRequest solicitud = repository.findById(idRequest)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        repository.rechazarSolicitud(idRequest, motivo);
        emailService.enviarMotivoRechazo(
                solicitud.getEmail(),
                solicitud.getFirstName(),
                motivo
        );
    }

    private String generarPasswordAleatorio() {
        return String.valueOf((int) (Math.random() * 899999) + 100000);
    }
}