package com.uteq.sgtic.services.admissions;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private String baseTemplate(String contenido) {
        return """
        <html>
        <body style="margin:0;padding:0;background:#f4f6f8;font-family:'Segoe UI',Arial,sans-serif;">
          <table width="100%" cellpadding="0" cellspacing="0" style="background:#f4f6f8;padding:40px 0;">
            <tr><td align="center">
              <table width="600" cellpadding="0" cellspacing="0" style="background:white;border-radius:12px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,0.08);">
                
                <!-- HEADER -->
                <tr>
                  <td style="background:#1b5e20;padding:32px 40px;text-align:center;">
                    <h1 style="color:white;margin:0;font-size:22px;font-weight:700;letter-spacing:1px;">SGTIC</h1>
                    <p style="color:rgba(255,255,255,0.8);margin:4px 0 0 0;font-size:13px;">Sistema de Gestión de Trabajos de Integración Curricular</p>
                  </td>
                </tr>

                <!-- CONTENIDO -->
                <tr>
                  <td style="padding:40px;">
                    """ + contenido + """
                  </td>
                </tr>

                <!-- FOOTER -->
                <tr>
                  <td style="background:#f8faf9;padding:24px 40px;border-top:1px solid #e5e7eb;text-align:center;">
                    <p style="color:#9ca3af;font-size:12px;margin:0;">© 2024 SGTIC - Universidad Técnica Estatal de Quevedo</p>
                    <p style="color:#9ca3af;font-size:12px;margin:4px 0 0 0;">Este es un correo automático, por favor no responder.</p>
                  </td>
                </tr>

              </table>
            </td></tr>
          </table>
        </body>
        </html>
        """;
    }

    private void enviar(String destinatario, String asunto, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(baseTemplate(htmlContent), true);
            mailSender.send(message);
            System.out.println("✅ Correo enviado a: " + destinatario);
        } catch (MessagingException e) {
            System.err.println("❌ Error al enviar correo: " + e.getMessage());
        }
    }

    public void enviarCredenciales(String destinatario, String nombreEstudiante, String username, String password) {
        String contenido = """
            <div style="text-align:center;margin-bottom:32px;">
              <div style="width:64px;height:64px;background:#dcfce7;border-radius:50%;display:inline-flex;align-items:center;justify-content:center;margin-bottom:16px;">
                <span style="font-size:32px;">✅</span>
              </div>
              <h2 style="color:#1b5e20;margin:0;font-size:24px;">¡Solicitud Aprobada!</h2>
            </div>

<<<<<<< HEAD
            <p style="color:#374151;font-size:15px;line-height:1.6;">¡Felicidades <strong>%s</strong>!</p>
=======
            <p style="color:#374151;font-size:15px;line-height:1.6;">¡Felicidades <strong>""" + nombreEstudiante + """
            </strong>!</p>
>>>>>>> master
            <p style="color:#374151;font-size:15px;line-height:1.6;">
              Tu solicitud ha sido <strong style="color:#1b5e20;">ACEPTADA</strong> por el coordinador.
              Ya puedes ingresar al sistema web de titulación.
            </p>

            <div style="background:#f0fdf4;border:1px solid #bbf7d0;border-radius:10px;padding:24px;margin:28px 0;">
              <p style="color:#166534;font-weight:700;font-size:13px;text-transform:uppercase;letter-spacing:0.5px;margin:0 0 16px 0;">
                🔐 Tus credenciales temporales
              </p>
<<<<<<< HEAD
              <table width="100%%" cellpadding="8" cellspacing="0">
                <tr>
                  <td style="color:#6b7280;font-size:14px;width:120px;">Usuario:</td>
                  <td style="color:#111827;font-weight:600;font-size:14px;">%s</td>
                </tr>
                <tr>
                  <td style="color:#6b7280;font-size:14px;">Contraseña:</td>
                  <td style="color:#111827;font-weight:600;font-size:14px;">%s</td>
=======
              <table width="100%" cellpadding="8" cellspacing="0">
                <tr>
                  <td style="color:#6b7280;font-size:14px;width:120px;">Usuario:</td>
                  <td style="color:#111827;font-weight:600;font-size:14px;">""" + username + """
                  </td>
                </tr>
                <tr>
                  <td style="color:#6b7280;font-size:14px;">Contraseña:</td>
                  <td style="color:#111827;font-weight:600;font-size:14px;">""" + password + """
                  </td>
>>>>>>> master
                </tr>
              </table>
            </div>

            <div style="background:#fef9c3;border:1px solid #fde68a;border-radius:8px;padding:16px;margin-bottom:28px;">
              <p style="color:#92400e;font-size:13px;margin:0;">
                ⚠️ <strong>Nota:</strong> Al ingresar por primera vez, el sistema te pedirá cambiar tu contraseña.
              </p>
            </div>

            <p style="color:#6b7280;font-size:14px;margin:0;">Atentamente,<br><strong style="color:#1b5e20;">SGTIC - Sistema de Gestión de Titulación</strong></p>
<<<<<<< HEAD
        """.formatted(nombreEstudiante, username, password);
=======
        """;
>>>>>>> master

        enviar(destinatario, "✅ SGTIC: Solicitud Aprobada - Credenciales de Acceso", contenido);
    }

    public void enviarMotivoRechazo(String destinatario, String nombreEstudiante, String motivo) {
        String contenido = """
            <div style="text-align:center;margin-bottom:32px;">
              <div style="width:64px;height:64px;background:#fee2e2;border-radius:50%;display:inline-flex;align-items:center;justify-content:center;margin-bottom:16px;">
                <span style="font-size:32px;">🚨</span>
              </div>
              <h2 style="color:#991b1b;margin:0;font-size:24px;">Solicitud Rechazada</h2>
            </div>

            <p style="color:#374151;font-size:15px;line-height:1.6;">Estimado/a <strong>%s</strong>,</p>
            <p style="color:#374151;font-size:15px;line-height:1.6;">
              Le informamos que su solicitud de inicio de titulación ha sido <strong style="color:#dc2626;">RECHAZADA</strong>.
            </p>

            <div style="background:#fef2f2;border:1px solid #fecaca;border-radius:10px;padding:24px;margin:28px 0;">
              <p style="color:#991b1b;font-weight:700;font-size:13px;text-transform:uppercase;margin:0 0 12px 0;">
                📋 Motivo del rechazo
              </p>
              <p style="color:#374151;font-size:14px;margin:0;line-height:1.6;">%s</p>
            </div>

            <p style="color:#374151;font-size:15px;line-height:1.6;">
              Por favor, revise las observaciones, corrija los datos según lo solicitado y vuelva a enviar su solicitud.
            </p>

            <p style="color:#6b7280;font-size:14px;margin:0;">Atentamente,<br><strong style="color:#1b5e20;">Coordinación de Carrera - SGTIC</strong></p>
        """.formatted(nombreEstudiante, motivo);

        enviar(destinatario, "🚨 SGTIC: Actualización de Solicitud de Titulación", contenido);
    }

    public void enviarAlertaCoordinador(String correoCoordinador, String nombreEstudiante) {
        String contenido = """
            <div style="text-align:center;margin-bottom:32px;">
              <div style="width:64px;height:64px;background:#fef3c7;border-radius:50%;display:inline-flex;align-items:center;justify-content:center;margin-bottom:16px;">
                <span style="font-size:32px;">⚠️</span>
              </div>
              <h2 style="color:#92400e;margin:0;font-size:24px;">Solicitud Pendiente</h2>
            </div>

            <p style="color:#374151;font-size:15px;line-height:1.6;">Estimado Coordinador,</p>
            <p style="color:#374151;font-size:15px;line-height:1.6;">
              El sistema SGTIC ha detectado que la solicitud del estudiante
              <strong>%s</strong> lleva más de <strong style="color:#d97706;">3 días</strong> en estado PENDIENTE.
            </p>

            <div style="background:#fffbeb;border:1px solid #fde68a;border-radius:10px;padding:20px;margin:28px 0;text-align:center;">
              <p style="color:#92400e;font-size:14px;margin:0;">
                ⏰ Por favor, ingrese al sistema a la brevedad posible para gestionar esta solicitud.
              </p>
            </div>

            <p style="color:#6b7280;font-size:14px;margin:0;">Atentamente,<br><strong style="color:#1b5e20;">Robot Automático SGTIC</strong></p>
        """.formatted(nombreEstudiante);

        enviar(correoCoordinador, "⚠️ SGTIC - ALERTA: Solicitud Pendiente de Revisión", contenido);
    }

    public void enviarNotificacionAsignacionDirector(String correoDocente, String nombreDocente, String tituloProyecto, String nombreEstudiante) {
        String contenido = """
            <div style="text-align:center;margin-bottom:32px;">
              <div style="width:64px;height:64px;background:#dbeafe;border-radius:50%;display:inline-flex;align-items:center;justify-content:center;margin-bottom:16px;">
                <span style="font-size:32px;">📋</span>
              </div>
              <h2 style="color:#1e40af;margin:0;font-size:24px;">Nueva Asignación</h2>
            </div>

            <p style="color:#374151;font-size:15px;line-height:1.6;">Estimado/a <strong>%s</strong>,</p>
            <p style="color:#374151;font-size:15px;line-height:1.6;">
              Le informamos que ha sido designado/a como <strong style="color:#1b5e20;">DIRECTOR</strong>
              para el siguiente proyecto de titulación:
            </p>

            <div style="background:#eff6ff;border:1px solid #bfdbfe;border-radius:10px;padding:24px;margin:28px 0;">
              <table width="100%%" cellpadding="8" cellspacing="0">
                <tr>
                  <td style="color:#6b7280;font-size:14px;width:120px;">Proyecto:</td>
                  <td style="color:#111827;font-weight:600;font-size:14px;">%s</td>
                </tr>
                <tr>
                  <td style="color:#6b7280;font-size:14px;">Estudiante:</td>
                  <td style="color:#111827;font-weight:600;font-size:14px;">%s</td>
                </tr>
              </table>
            </div>

            <p style="color:#374151;font-size:15px;line-height:1.6;">
              Por favor, ingrese al sistema SGTIC para revisar los detalles de la propuesta y comenzar con el proceso de tutoría.
            </p>

            <p style="color:#6b7280;font-size:14px;margin:0;">Atentamente,<br><strong style="color:#1b5e20;">Coordinación de Carrera - SGTIC</strong></p>
        """.formatted(nombreDocente, tituloProyecto, nombreEstudiante);

        enviar(correoDocente, "📋 SGTIC: Nueva Asignación - Director de Trabajo de Integración Curricular", contenido);
    }
}