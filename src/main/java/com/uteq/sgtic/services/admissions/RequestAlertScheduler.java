package com.uteq.sgtic.services.admissions;

import com.uteq.sgtic.projections.admissions.PendingAlertProjection;
import com.uteq.sgtic.repository.admissions.AccessRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequestAlertScheduler {
    @Autowired
    private AccessRequestRepository repository;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 8 * * *")
    public void revisarSolicitudesAtrasadas() {
        System.out.println("🤖 Robot SGTIC: Buscando solicitudes atrasadas...");

        List<PendingAlertProjection> atrasadas = repository.buscarSolicitudesAtrasadas();

        if (atrasadas.isEmpty()) {
            System.out.println("🤖 Robot SGTIC: Todo al día. No hay solicitudes atrasadas.");
            return;
        }

        for (PendingAlertProjection alerta : atrasadas) {
            String nombreCompleto = alerta.getNombres() + " " + alerta.getApellidos();
            emailService.enviarAlertaCoordinador(alerta.getCorreoCoordinador(), nombreCompleto);
        }
    }
}
