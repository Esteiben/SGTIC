package com.uteq.sgtic.services.admissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoRechazo(String destinatario, String nombreEstudiante, String motivo) {
        try {
            // Creamos el "sobre" y la "carta"
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinatario);
            message.setSubject("🚨 SGTIC: Actualización de Solicitud de Titulación");
            message.setText("Estimado/a " + nombreEstudiante + ",\n\n" +
                    "Le informamos que su solicitud de inicio de titulación ha sido RECHAZADA.\n\n" +
                    "Motivo del rechazo:\n" + motivo + "\n\n" +
                    "Por favor, revise las observaciones, corrija lo necesario y vuelva a enviar su solicitud.\n\n" +
                    "Atentamente,\nCoordinación de Carrera.");

            // El cartero entrega la carta
            mailSender.send(message);
            System.out.println("Correo enviado exitosamente a: " + destinatario);

        } catch (Exception e) {
            // Si el internet falla, no se cae el sistema, solo avisa.
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    public void enviarAlertaCoordinador(String correoCoordinador, String nombreEstudiante) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(correoCoordinador);
            message.setSubject("⚠️ SGTIC - ALERTA: Solicitud Pendiente de Revisión");
            message.setText("Estimado Coordinador,\n\n" +
                    "El sistema SGTIC ha detectado que la solicitud de inicio de titulación del estudiante " +
                    nombreEstudiante + " lleva más de 3 días en estado PENDIENTE.\n\n" +
                    "Por favor, ingrese al sistema a la brevedad posible para gestionar esta solicitud.\n\n" +
                    "Atentamente,\nRobot Automático SGTIC.");

            mailSender.send(message);
            System.out.println("🤖 Alerta enviada al coordinador: " + correoCoordinador);
        } catch (Exception e) {
            System.err.println("Error al enviar alerta automática: " + e.getMessage());
        }
    }
}
