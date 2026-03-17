package com.uteq.sgtic.controllers;

import com.uteq.sgtic.entities.ChatMessageEntity;
import com.uteq.sgtic.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatHistoryController {

    @Autowired
    private ChatMessageRepository messageRepository;

    @GetMapping("/historial")
    public List<ChatMessageEntity> getHistorial() {
        return messageRepository.findAllByOrderByFechaEnvioAsc();
    }

    @GetMapping("/historial/estudiante/{correo}")
    public List<ChatMessageEntity> getHistorialEstudiante(@PathVariable String correo) {
        // Implementar lógica para buscar mensajes por estudiante
        return messageRepository.findByUsuarioCorreo(correo);
    }
}