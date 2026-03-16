package com.uteq.sgtic.controllers.teacherAssignmentController;

import com.uteq.sgtic.dtos.teacherAssignmentDTO.TeacherAssignmentDTO;
import com.uteq.sgtic.dtos.teacherAssignmentDTO.PendingProjectDTO;
import com.uteq.sgtic.services.teacherAssignmentService.TeacherAssignmentService;
import com.uteq.sgtic.dtos.iaMatchResultDTO.AiMatchResultDTO;
import com.uteq.sgtic.services.iaAssignmentService.AiAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/asignaciones")
@CrossOrigin(origins = "*")
public class TeacherAssignmentController {

    @Autowired
    private TeacherAssignmentService asignacionService;

    @Autowired
    private AiAssignmentService aiAssignmentService;

    @GetMapping("/docentes-disponibles/{idUsuario}")
    public List<TeacherAssignmentDTO> getDocentes(@PathVariable Integer idUsuario) {
        return asignacionService.listarDocentesParaAsignacion(idUsuario);
    }

    @GetMapping("/proyectos-pendientes/{idCoordinador}")
    public ResponseEntity<List<PendingProjectDTO>> getProyectosPendientes(@PathVariable Integer idCoordinador) {
        List<PendingProjectDTO> proyectos = asignacionService.listarPropuestasPendientes(idCoordinador);
        return ResponseEntity.ok(proyectos);
    }

    @PostMapping("/asignar")
    public ResponseEntity<?> asignarDirector(@RequestBody Map<String, Integer> payload) {
        asignacionService.ejecutarAsignacion(
                payload.get("idPropuesta"),
                payload.get("idDocente"),
                payload.get("idPeriodo")
        );
        return ResponseEntity.ok().body("{\"message\": \"Asignación procesada exitosamente\"}");
    }

    @PostMapping("/sugerir-ia")
    public ResponseEntity<List<AiMatchResultDTO>> sugerirIA(@RequestBody Map<String, Object> payload) {
        String titulo = (String) payload.get("titulo");
        String descripcion = (String) payload.get("descripcion");
        List<Map<String, Object>> docentesFull = (List<Map<String, Object>>) payload.get("docentes");
        List<Map<String, Object>> docentesSimplificados = docentesFull.stream().map(d -> {
            Map<String, Object> simplified = new HashMap<>();
            simplified.put("idDocente", d.get("idDocente"));
            simplified.put("nombreCompleto", d.get("nombreCompleto"));
            simplified.put("especialidades", d.get("specializations"));
            return simplified;
        }).collect(Collectors.toList());

        List<AiMatchResultDTO> sugerencias = aiAssignmentService.obtenerSugerencias(titulo, descripcion, (List<Object>)(Object)docentesSimplificados);
        return ResponseEntity.ok(sugerencias);
    }
}