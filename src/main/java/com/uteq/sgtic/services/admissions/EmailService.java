package com.uteq.sgtic.services.admissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void enviarCredenciales(String destinatario, String nombreEstudiante, String username, String password) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinatario);
            message.setSubject("✅ SGTIC: Solicitud Aprobada - Credenciales de Acceso");
            message.setText("¡Felicidades " + nombreEstudiante + "!\n\n" +
                    "Tu solicitud ha sido ACEPTADA por el coordinador. Ya puedes ingresar al sistema web de titulación.\n\n" +
                    "Tus credenciales temporales son:\n" +
                    "Usuario: " + username + "\n" +
                    "Contraseña: " + password + "\n\n" +
                    "Nota: Al ingresar por primera vez, el sistema te pedirá cambiar tu contraseña.\n\n" +
                    "Atentamente,\nSGTIC - Sistema de Gestión de Titulación.");

            mailSender.send(message);
            System.out.println("Credenciales enviadas exitosamente a: " + destinatario);
        } catch (Exception e) {
            System.err.println("Error al enviar credenciales: " + e.getMessage());
        }
    }

    public void enviarMotivoRechazo(String destinatario, String nombreEstudiante, String motivo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinatario);
            message.setSubject("🚨 SGTIC: Actualización de Solicitud de Titulación");
            message.setText("Estimado/a " + nombreEstudiante + ",\n\n" +
                    "Le informamos que su solicitud de inicio de titulación ha sido RECHAZADA.\n\n" +
                    "Motivo del rechazo:\n" + motivo + "\n\n" +
                    "Por favor, revise las observaciones, corrija los datos según lo solicitado y vuelva a enviar su solicitud.\n\n" +
                    "Atentamente,\nCoordinación de Carrera.");

            mailSender.send(message);
            System.out.println("Correo de rechazo enviado a: " + destinatario);
        } catch (Exception e) {
            System.err.println("Error al enviar el correo de rechazo: " + e.getMessage());
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

    public void enviarNotificacionAsignacionDirector(String correoDocente, String nombreDocente, String tituloProyecto, String nombreEstudiante) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(correoDocente);
            message.setSubject("📋 SGTIC: Nueva Asignación - Director de Trabajo de Integración Curricular");
            message.setText("Estimado/a " + nombreDocente + ",\n\n" +
                    "Le informamos que el Consejo Directivo de la Facultad le ha designado como DIRECTOR para el siguiente proyecto de titulación:\n\n" +
                    "Proyecto: " + tituloProyecto + "\n" +
                    "Estudiante: " + nombreEstudiante + "\n\n" +
                    "Por favor, ingrese al sistema SGTIC para revisar los detalles de la propuesta y comenzar con el proceso de tutoría.\n\n" +
                    "Atentamente,\nCoordinación de Carrera - SGTIC.");

            mailSender.send(message);
            System.out.println("📧 Notificación de asignación enviada al docente: " + correoDocente);
        } catch (Exception e) {
            System.err.println("Error al enviar notificación al docente: " + e.getMessage());
        }
    }
}