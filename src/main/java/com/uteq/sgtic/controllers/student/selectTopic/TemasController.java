package com.uteq.sgtic.controllers.student.selectTopic;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uteq.sgtic.dtos.student.selectTopic.StudentProposalSummaryDTO;
import com.uteq.sgtic.dtos.student.selectTopic.TemaDTO;
import com.uteq.sgtic.dtos.student.selectTopic.TopicSelectionRequestDTO;
import com.uteq.sgtic.dtos.student.selectTopic.TopicSelectionResponseDTO;
import com.uteq.sgtic.dtos.student.selectTopic.TopicSelectionStatusDTO;
import com.uteq.sgtic.repository.student.selectTopic.ProcessSetupRepository;
import com.uteq.sgtic.services.AzureStorageConfig;
import com.uteq.sgtic.services.student.selectTopic.IProcessSetupService;
import com.uteq.sgtic.services.student.selectTopic.ITopicSelectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/temas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TemasController {

    private final IProcessSetupService processSetupService;
    private final ITopicSelectionService topicSelectionService;
    private final AzureStorageConfig azureStorageConfig;

    @GetMapping("/disponibles")
    public ResponseEntity<List<TemaDTO>> getTemasDisponibles(
            @RequestParam Integer idPeriodo,
            @RequestParam Integer idOpcion,
            Authentication authentication
    ) {
        Integer idEstudiante = processSetupService.getIdEstudianteByUsername(authentication.getName());
        return ResponseEntity.ok(processSetupService.getTemasDisponibles(idPeriodo, idEstudiante, idOpcion));
    }

    // =======================================================
    // ENDPOINT ESTADO ACTUALIZADO (Conectado a la BD)
    // =======================================================
    @GetMapping("/estado")
    public ResponseEntity<TopicSelectionStatusDTO> getEstado(
            @RequestParam Integer idPeriodo,
            Authentication authentication
    ) {
        Integer idEstudiante = processSetupService.getIdEstudianteByUsername(authentication.getName());
        // Se conecta a la BD para validar plazos, intentos y estados de las propuestas
        return ResponseEntity.ok(topicSelectionService.getEstado(idEstudiante, idPeriodo));
    }

    @GetMapping("/propuestas-estudiante")
    public ResponseEntity<List<StudentProposalSummaryDTO>> getStudentProposals(
            @RequestParam Integer idPeriodo,
            Authentication authentication
    ) {
        Integer idEstudiante = processSetupService.getIdEstudianteByUsername(authentication.getName());
        return ResponseEntity.ok(processSetupService.getStudentProposals(idPeriodo, idEstudiante));
    }

    @PostMapping("/seleccion")
    public ResponseEntity<TopicSelectionResponseDTO> seleccionarOCambiarTema(
            @RequestBody TopicSelectionRequestDTO request,
            Authentication authentication
    ) {
        Integer idEstudiante = processSetupService.getIdEstudianteByUsername(authentication.getName());
        TopicSelectionResponseDTO response = topicSelectionService.procesarSeleccionOCambio(idEstudiante, request);

        if (Boolean.TRUE.equals(response.getExito())) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping(value = "/propuesta-estudiante", consumes = {"multipart/form-data"})
    public ResponseEntity<TopicSelectionResponseDTO> registrarPropuesta(
            @RequestParam("idOpcion") Integer idOpcion,
            @RequestParam("idPeriodo") Integer idPeriodo,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "documento", required = false) MultipartFile documento,
            Authentication authentication
    ) {
        Integer idEstudiante = processSetupService.getIdEstudianteByUsername(authentication.getName());
        String urlDocumento = null;

        try {
            // Si hay un documento adjunto, lo subimos a Azure primero
            if (documento != null && !documento.isEmpty()) {
                urlDocumento = azureStorageConfig.subirDocumento(documento);
            }
            
            TopicSelectionResponseDTO response = topicSelectionService.registrarPropuesta(
                    idEstudiante, idPeriodo, idOpcion, titulo, descripcion, urlDocumento
            );

            if (Boolean.TRUE.equals(response.getExito())) return ResponseEntity.ok(response);
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            // Manejo de errores en caso de que Azure falle
            return ResponseEntity.badRequest().body(
                new TopicSelectionResponseDTO(false, "Error al subir el documento a la nube: " + e.getMessage())
            );
        }
    }

    @PutMapping(value = "/propuestas-estudiante/{idPropuesta}", consumes = {"multipart/form-data"})
    public ResponseEntity<TopicSelectionResponseDTO> actualizarPropuesta(
            @PathVariable("idPropuesta") Integer idPropuesta,
            @RequestParam("idOpcion") Integer idOpcion,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "documento", required = false) MultipartFile documento,
            Authentication authentication
    ) {
        Integer idEstudiante = processSetupService.getIdEstudianteByUsername(authentication.getName());
        String urlDocumento = null;

        try {
            // Si el estudiante adjunta un nuevo documento, lo subimos a Azure
            if (documento != null && !documento.isEmpty()) {
                urlDocumento = azureStorageConfig.subirDocumento(documento);
            }
            
            TopicSelectionResponseDTO response = topicSelectionService.actualizarPropuesta(
                    idPropuesta, idEstudiante, idOpcion, titulo, descripcion, urlDocumento
            );

            if (Boolean.TRUE.equals(response.getExito())) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            // Manejo de errores en caso de que Azure falle
            return ResponseEntity.badRequest().body(
                new TopicSelectionResponseDTO(false, "Error al subir el nuevo documento a la nube: " + e.getMessage())
            );
        }
    }

    // ==========================================
    // ENDPOINTS DE HISTORIAL (Línea de vida)
    // ==========================================

    @GetMapping("/historial-selecciones")
    public ResponseEntity<List<ProcessSetupRepository.TopicSelectionHistoryProjection>> getHistorialSelecciones(
            @RequestParam Integer idPeriodo, 
            Authentication authentication
    ) {
        Integer idEstudiante = processSetupService.getIdEstudianteByUsername(authentication.getName());
        return ResponseEntity.ok(processSetupService.getTopicSelectionHistory(idPeriodo, idEstudiante));
    }

    @GetMapping("/propuestas-estudiante/{idPropuesta}/historial")
    public ResponseEntity<List<ProcessSetupRepository.StudentProposalHistoryProjection>> getHistorialPropuesta(
            @PathVariable Integer idPropuesta
    ) {
        return ResponseEntity.ok(processSetupService.getStudentProposalHistory(idPropuesta));
    }
}