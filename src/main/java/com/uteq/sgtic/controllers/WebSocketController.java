package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.ChatMessage;
import com.uteq.sgtic.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage chat(@DestinationVariable String roomId, ChatMessage message){
        System.out.println(" ===== MENSAJE RECIBIDO =====");
        System.out.println("   Sala: " + roomId);
        System.out.println("   Usuario: " + message.getUser());
        System.out.println("   Mensaje: " + message.getMessage());
        System.out.println("   Timestamp: " + message.getTimestamp());

        // Verificar si es una respuesta a otro mensaje
        if (message.getReplyTo() != null) {
            System.out.println("   ↪ RESPONDIENDO A:");
            System.out.println("      ID: " + message.getReplyTo().getId());
            System.out.println("      Usuario: " + message.getReplyTo().getUser());
            System.out.println("      Mensaje: " + message.getReplyTo().getMessage());
        }

        // Guardar en base de datos en segundo plano para no bloquear
        new Thread(() -> {
            try {
                chatService.guardarMensaje(roomId, message);
                System.out.println(" Mensaje enviado a ChatService para guardar");
            } catch (Exception e) {
                System.err.println(" Error al guardar mensaje: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        return message;
    }
}