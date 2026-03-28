package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.ChatMessageDTO;
import com.uteq.sgtic.entities.ChatMessageEntity;
import com.uteq.sgtic.repository.ChatConversationRepository;
import com.uteq.sgtic.repository.ChatMessageRepository;
import com.uteq.sgtic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    @Autowired
    private ChatMessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatConversationRepository conversationRepository;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessageDTO processMessage(
            @DestinationVariable String roomId,
            ChatMessageDTO messageDTO) {

        System.out.println(">>> Mensaje recibido en sala: " + roomId);

        try {
            Long idConv = messageDTO.getIdConversacion();

            if (!conversationRepository.existsById(idConv)) {
                System.err.println("❌ Conversación no encontrada: " + idConv);
                return messageDTO;
            }

            ChatMessageEntity entity = new ChatMessageEntity();
            entity.setMensaje(messageDTO.getMessage());
            entity.setIdConversacion(idConv);
            entity.setFechaEnvio(LocalDateTime.now());
            entity.setEstado("enviado");

            if (messageDTO.getReplyTo() != null) {
                entity.setReplyToId(messageDTO.getReplyTo().getId());
                entity.setReplyToMessage(messageDTO.getReplyTo().getMessage());
                entity.setReplyToUser(messageDTO.getReplyTo().getUser());
            }

            userRepository.findByEmail(messageDTO.getUser()).ifPresentOrElse(
                    user -> entity.setIdRemitente(Long.valueOf(user.getId())),
                    () -> System.err.println("⚠ Usuario no encontrado: "
                            + messageDTO.getUser())
            );

            ChatMessageEntity guardado = messageRepository.save(entity);
            messageDTO.setId(guardado.getId());
            System.out.println(" Guardado con ID: " + guardado.getId());

        } catch (Exception e) {
            System.err.println(" Error crítico: " + e.getMessage());
            e.printStackTrace();
        }

        return messageDTO;
    }
}