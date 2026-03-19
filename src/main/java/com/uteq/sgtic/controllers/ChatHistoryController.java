package com.uteq.sgtic.controllers;

import com.uteq.sgtic.entities.ChatMessageEntity;
import com.uteq.sgtic.repository.ChatMessageRepository;
import com.uteq.sgtic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatHistoryController {

    @Autowired
    private ChatMessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/historial/{idConversacion}")
    public List<Map<String, Object>> getHistorialPorConversacion(
            @PathVariable Long idConversacion) {

        List<ChatMessageEntity> mensajes = messageRepository
                .findByConversacionPersonalizada(idConversacion);

        List<Map<String, Object>> resultado = new ArrayList<>();

        for (ChatMessageEntity msg : mensajes) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", msg.getId());
            map.put("mensaje", msg.getMensaje());
            map.put("idRemitente", msg.getIdRemitente());
            map.put("idConversacion", msg.getIdConversacion()); // ← fix agregado
            map.put("fechaEnvio", msg.getFechaEnvio());
            map.put("replyToId", msg.getReplyToId());
            map.put("replyToMessage", msg.getReplyToMessage());
            map.put("replyToUser", msg.getReplyToUser());

            String correoReal = userRepository
                    .findById(msg.getIdRemitente().intValue())
                    .map(user -> user.getEmail())
                    .orElse("usuario_desconocido@uteq.edu.ec");

            map.put("correoRemitente", correoReal);
            resultado.add(map);
        }

        return resultado;
    }

    @GetMapping("/historial")
    public List<ChatMessageEntity> getHistorialGlobal() {
        return messageRepository.findAllByOrderByFechaEnvioAsc();
    }
}