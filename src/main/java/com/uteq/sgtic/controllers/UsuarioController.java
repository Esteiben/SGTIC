package com.uteq.sgtic.controllers;

import com.uteq.sgtic.entities.ChatConversation;
import com.uteq.sgtic.entities.Student;
import com.uteq.sgtic.entities.User;
import com.uteq.sgtic.repository.ChatConversationRepository;
import com.uteq.sgtic.repository.StudentRepository;
import com.uteq.sgtic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatConversationRepository conversationRepository;

    @Autowired
    private StudentRepository studentRepository;

    // ── Endpoint para ESTUDIANTES: carga coordinadores y crea conversaciones ──
    @GetMapping("/coordinadores")
    public List<Map<String, Object>> getCoordinadores(
            @RequestParam(required = false) String emailEstudiante) {

        List<User> todosLosUsuarios = userRepository.findAll();
        List<Map<String, Object>> respuesta = new ArrayList<>();

        Optional<User> usuarioEstudianteOpt = (emailEstudiante != null)
                ? userRepository.findByEmail(emailEstudiante)
                : Optional.empty();

        Optional<Student> studentOpt = usuarioEstudianteOpt.isPresent()
                ? studentRepository.findByUser(usuarioEstudianteOpt.get())
                : Optional.empty();

        for (User user : todosLosUsuarios) {
            Map<String, Object> dto = new HashMap<>();
            dto.put("fullName", user.getFirstName() + " " + user.getLastName());
            dto.put("email", user.getEmail());

            Long idConv = null;

            if (usuarioEstudianteOpt.isPresent() && studentOpt.isPresent()) {
                User usuarioEstudiante = usuarioEstudianteOpt.get();
                Student student = studentOpt.get();

                // Evitar que el estudiante aparezca como su propio contacto
                if (user.getId().equals(usuarioEstudiante.getId())) continue;

                // Solo mostrar usuarios con rol coordinador o director
                boolean esCoordinadorODirector = user.getRoles().stream()
                        .anyMatch(r -> r.getName().equals("coordinador_facultad")
                                || r.getName().equals("coordinador_carrera")
                                || r.getName().equals("director_trabajo_titulacion"));

                if (!esCoordinadorODirector) continue;

                Long idEstudianteReal = student.getIdStudent().longValue();

                Optional<ChatConversation> convExistente = conversationRepository
                        .findByIdEstudianteAndIdCoordinador(
                                idEstudianteReal,
                                user.getId().longValue()
                        );

                if (convExistente.isPresent()) {
                    idConv = convExistente.get().getId();
                } else {
                    ChatConversation nueva = new ChatConversation();
                    nueva.setIdEstudiante(idEstudianteReal);
                    nueva.setIdCoordinador(user.getId().longValue());
                    nueva.setEstado("activa");
                    nueva.setFechaInicio(LocalDateTime.now());
                    nueva.setFechaActualizacion(LocalDateTime.now());
                    idConv = conversationRepository.save(nueva).getId();
                }
            }

            dto.put("idConversacion", idConv);
            respuesta.add(dto);
        }

        return respuesta;
    }

    // ── Endpoint para COORDINADORES/DIRECTORES: carga sus estudiantes ──
    @GetMapping("/mis-estudiantes")
    public List<Map<String, Object>> getMisEstudiantes(
            @RequestParam String emailCoordinador) {

        Optional<User> coordOpt = userRepository.findByEmail(emailCoordinador);
        if (coordOpt.isEmpty()) return new ArrayList<>();

        User coordinador = coordOpt.get();
        List<Map<String, Object>> respuesta = new ArrayList<>();

        List<ChatConversation> conversaciones = conversationRepository
                .findByIdCoordinador(coordinador.getId().longValue());

        for (ChatConversation conv : conversaciones) {
            studentRepository.findById(conv.getIdEstudiante().intValue()).ifPresent(student -> {
                Map<String, Object> dto = new HashMap<>();
                dto.put("fullName", student.getUser().getFirstName() + " "
                        + student.getUser().getLastName());
                dto.put("email", student.getUser().getEmail());
                dto.put("idConversacion", conv.getId());
                respuesta.add(dto);
            });
        }

        return respuesta;
    }
}