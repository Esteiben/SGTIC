package com.uteq.sgtic.controllers.student.selectTopic;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/disponibles")
    public ResponseEntity<List<TemaDTO>> getTemasDisponibles(
            @RequestParam Integer idPeriodo,
            @RequestParam Integer idOpcion,
            Authentication authentication
    ) {
        Integer idEstudiante = processSetupService.getIdEstudianteByUsername(authentication.getName());
        
        return ResponseEntity.ok(
                processSetupService.getTemasDisponibles(idPeriodo, idEstudiante, idOpcion)
        );
    }

    @GetMapping("/estado")
    public ResponseEntity<TopicSelectionStatusDTO> getEstado(
            @RequestParam Integer idPeriodo,
            Authentication authentication
    ) {
        Integer idEstudiante = processSetupService.getIdEstudianteByUsername(authentication.getName());

        // TODO: En el futuro, llenar este DTO llamando a una función de PostgreSQL 
        // pasándole el idEstudiante y el idPeriodo.
        TopicSelectionStatusDTO dto = new TopicSelectionStatusDTO();
        dto.setEstadoTitulacion("EN_PROCESO");
        dto.setTipoTemaActual("NINGUNO");
        dto.setFueraDePlazo(false);
        dto.setDesactivadoPorPlazo(false);
        dto.setTieneProcesoTema(false);
        dto.setTieneSeleccionBanco(false);
        dto.setPuedeSeleccionar(true);
        dto.setPuedeProponer(true);
        dto.setPuedeCambiarTema(false);
        dto.setCambiosTemaRealizados(0);
        dto.setFechaLimiteSeleccion(null);
        dto.setMensaje("Selecciona una modalidad y luego el tema de titulación.");
        
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/propuestas-estudiante")
    public ResponseEntity<List<StudentProposalSummaryDTO>> getStudentProposals(
            @RequestParam Integer idPeriodo,
            Authentication authentication
    ) {
        Integer idEstudiante = processSetupService.getIdEstudianteByUsername(authentication.getName());
        
        return ResponseEntity.ok(
                processSetupService.getStudentProposals(idPeriodo, idEstudiante)
        );
    }

    @PostMapping("/seleccion")
    public ResponseEntity<TopicSelectionResponseDTO> seleccionarOCambiarTema(
            @RequestBody TopicSelectionRequestDTO request,
            Authentication authentication
    ) {
        Integer idEstudiante = processSetupService.getIdEstudianteByUsername(authentication.getName());
        
        TopicSelectionResponseDTO response =
                topicSelectionService.procesarSeleccionOCambio(idEstudiante, request);

        if (Boolean.TRUE.equals(response.getExito())) {
            return ResponseEntity.ok(response);
        }
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

        // Si se envió un archivo, se debe procesar (ej. subir a Azure Blob Storage)
        if (documento != null && !documento.isEmpty()) {
            // TODO: Descomentar e implementar tu servicio de Azure
            // urlDocumento = azureStorageService.uploadFile(documento);
        }
        
        // Llamada al servicio que ejecuta la función SQL
        TopicSelectionResponseDTO response = topicSelectionService.registrarPropuesta(
                idEstudiante, 
                idPeriodo, 
                idOpcion, 
                titulo, 
                descripcion, 
                urlDocumento
        );

        if (Boolean.TRUE.equals(response.getExito())) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
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