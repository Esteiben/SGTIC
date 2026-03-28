package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long id;

    @Column(name = "id_conversacion")
    private Long idConversacion;

    @Column(name = "id_remitente")
    private Long idRemitente;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio = LocalDateTime.now();

    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;

    private String estado = "enviado";

    // =============================================
    // NUEVOS CAMPOS PARA LA FUNCIONALIDAD DE RESPUESTA
    // =============================================

    /**
     * ID del mensaje al que se está respondiendo
     */
    @Column(name = "reply_to_id")
    private Long replyToId;

    /**
     * Contenido del mensaje original (para mostrarlo sin necesidad de consulta adicional)
     */
    @Column(name = "reply_to_message", length = 500)
    private String replyToMessage;

    /**
     * Usuario que envió el mensaje original
     */
    @Column(name = "reply_to_user", length = 255)
    private String replyToUser;
}