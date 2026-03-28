package com.uteq.sgtic.services;

import com.uteq.sgtic.dtos.ChatMessage;
import com.uteq.sgtic.entities.*;
import com.uteq.sgtic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ChatConversationRepository conversationRepository;

    @Autowired
    private ChatMessageRepository messageRepository;

    @Transactional
    public void guardarMensaje(String roomId, ChatMessage messageDTO) {
        try {
            System.out.println("\n🔵 ===== GUARDANDO MENSAJE EN BD =====");
            System.out.println("   Room: " + roomId);
            System.out.println("   Usuario: " + messageDTO.getUser());
            System.out.println("   Mensaje: " + messageDTO.getMessage());

            // Verificar si es una respuesta
            if (messageDTO.getReplyTo() != null) {
                System.out.println("   ↪️ RESPONDE A:");
                System.out.println("      ID: " + messageDTO.getReplyTo().getId());
                System.out.println("      Usuario: " + messageDTO.getReplyTo().getUser());
                System.out.println("      Mensaje: " + messageDTO.getReplyTo().getMessage());
            }

            // Buscar usuario por email
            String email = messageDTO.getUser();
            if (!email.contains("@")) {
                email = messageDTO.getUser() + "@uteq.edu.ec";
            }

            var credenciales = userRepository.findCredentialsByEmail(email);

            if (credenciales.isEmpty()) {
                System.out.println("⚠️ Usuario no encontrado: " + messageDTO.getUser());
                return;
            }

            Integer userId = credenciales.get().getUserId();
            System.out.println("✅ Usuario encontrado ID: " + userId);

            // =============================================
            // AHORA GUARDAMOS MENSAJES DE CUALQUIER SALA
            // =============================================

            // Buscar si el usuario es estudiante
            Optional<Student> estudianteOpt = studentRepository.findByUserId(userId);

            if (estudianteOpt.isPresent()) {
                // Es un ESTUDIANTE
                Student estudiante = estudianteOpt.get();
                System.out.println("🎓 Estudiante encontrado ID: " + estudiante.getIdStudent());

                // Usar coordinador fijo
                Long idCoordinador = 15L; // ID del coordinador

                // Buscar o crear conversación
                ChatConversation conversation = conversationRepository
                        .findByEstudianteAndCoordinador(estudiante.getIdStudent().longValue(), idCoordinador)
                        .orElseGet(() -> {
                            System.out.println("🆕 Creando nueva conversación para estudiante");
                            ChatConversation newConv = new ChatConversation();
                            newConv.setIdEstudiante(estudiante.getIdStudent().longValue());
                            newConv.setIdCoordinador(idCoordinador);
                            newConv.setFechaInicio(LocalDateTime.now());
                            newConv.setFechaActualizacion(LocalDateTime.now());
                            newConv.setEstado("activa");
                            return conversationRepository.save(newConv);
                        });

                System.out.println("💬 Conversación ID: " + conversation.getId());

                // Guardar el mensaje
                ChatMessageEntity message = new ChatMessageEntity();
                message.setIdConversacion(conversation.getId());
                message.setIdRemitente(userId.longValue());
                message.setMensaje(messageDTO.getMessage());
                message.setFechaEnvio(LocalDateTime.now());
                message.setEstado("enviado");

                // =============================================
                // GUARDAR INFORMACIÓN DE RESPUESTA SI EXISTE
                // =============================================
                if (messageDTO.getReplyTo() != null) {
                    message.setReplyToId(messageDTO.getReplyTo().getId().longValue());
                    message.setReplyToMessage(messageDTO.getReplyTo().getMessage());
                    message.setReplyToUser(messageDTO.getReplyTo().getUser());
                    System.out.println("   ✅ Información de respuesta guardada");
                }

                messageRepository.save(message);
                System.out.println("✅ Mensaje de ESTUDIANTE guardado con ID: " + message.getId());

                // Actualizar fecha de la conversación
                conversation.setFechaActualizacion(LocalDateTime.now());
                conversationRepository.save(conversation);

            } else {
                // Es COORDINADOR u otro rol
                System.out.println("👤 Usuario no es estudiante (probablemente coordinador)");

                // Buscar o crear conversación para coordinador
                ChatConversation conversation = conversationRepository
                        .findById(1L)
                        .orElseGet(() -> {
                            System.out.println("🆕 Creando nueva conversación para coordinador");
                            ChatConversation newConv = new ChatConversation();
                            newConv.setIdEstudiante(0L);
                            newConv.setIdCoordinador(userId.longValue());
                            newConv.setFechaInicio(LocalDateTime.now());
                            newConv.setFechaActualizacion(LocalDateTime.now());
                            newConv.setEstado("activa");
                            return conversationRepository.save(newConv);
                        });

                // Guardar el mensaje del coordinador
                ChatMessageEntity message = new ChatMessageEntity();
                message.setIdConversacion(conversation.getId());
                message.setIdRemitente(userId.longValue());
                message.setMensaje(messageDTO.getMessage());
                message.setFechaEnvio(LocalDateTime.now());
                message.setEstado("enviado");

                // =============================================
                // GUARDAR INFORMACIÓN DE RESPUESTA SI EXISTE
                // =============================================
                if (messageDTO.getReplyTo() != null) {
                    message.setReplyToId(messageDTO.getReplyTo().getId().longValue());
                    message.setReplyToMessage(messageDTO.getReplyTo().getMessage());
                    message.setReplyToUser(messageDTO.getReplyTo().getUser());
                    System.out.println("   ✅ Información de respuesta guardada");
                }

                messageRepository.save(message);
                System.out.println("✅ Mensaje de COORDINADOR guardado con ID: " + message.getId());
            }

            System.out.println("🔵 ===== MENSAJE GUARDADO CORRECTAMENTE =====\n");

        } catch (Exception e) {
            System.err.println("❌ Error guardando mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }
}