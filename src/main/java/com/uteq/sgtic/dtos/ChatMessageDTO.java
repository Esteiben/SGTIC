package com.uteq.sgtic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private Long id;              // Cambiado a Long
    private String message;
    private String user;          // Debe ser el EMAIL del remitente
    private Long idConversacion;  // Cambiado a Long
    private ReplyToDTO replyTo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplyToDTO {
        private Long id;          // Cambiado a Long
        private String message;
        private String user;
    }
}