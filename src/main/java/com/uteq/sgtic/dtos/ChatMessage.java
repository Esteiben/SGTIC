package com.uteq.sgtic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String message;
    private String user;
    private LocalDateTime timestamp;
    private ReplyTo replyTo; // 👈 NUEVO CAMPO PARA RESPUESTAS

    // Constructor para mensajes sin timestamp
    public ChatMessage(String message, String user) {
        this.message = message;
        this.user = user;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor con replyTo
    public ChatMessage(String message, String user, ReplyTo replyTo) {
        this.message = message;
        this.user = user;
        this.timestamp = LocalDateTime.now();
        this.replyTo = replyTo;
    }

    /**
     * Clase interna que representa el mensaje al que se está respondiendo
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplyTo {
        private Long id;           // ID del mensaje original
        private String message;     // Contenido del mensaje original
        private String user;        // Usuario que envió el mensaje original
    }
}