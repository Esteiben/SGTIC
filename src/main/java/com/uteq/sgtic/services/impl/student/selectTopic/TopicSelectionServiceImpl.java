package com.uteq.sgtic.services.impl.student.selectTopic;

import com.uteq.sgtic.dtos.student.selectTopic.TopicSelectionRequestDTO;
import com.uteq.sgtic.dtos.student.selectTopic.TopicSelectionResponseDTO;
import com.uteq.sgtic.dtos.student.selectTopic.TopicSelectionStatusDTO;
import com.uteq.sgtic.repository.student.selectTopic.ProcessSetupRepository;
import com.uteq.sgtic.repository.student.selectTopic.TopicSelectionProjection;
import com.uteq.sgtic.repository.student.selectTopic.TopicSelectionRepository;
import com.uteq.sgtic.services.student.selectTopic.ITopicSelectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TopicSelectionServiceImpl implements ITopicSelectionService {

    private final TopicSelectionRepository repository;
    private final ProcessSetupRepository processSetupRepository;

    // ==============================================================================
    // 1. EL GUARDIÁN: Lee las reglas de negocio estrictas desde PostgreSQL
    // ==============================================================================
    @Override
    public TopicSelectionStatusDTO getEstado(Integer idEstudiante, Integer idPeriodo) {
        TopicSelectionRepository.TopicSelectionStatusProjection proj = repository.getEstadoSeleccion(idEstudiante, idPeriodo);
        TopicSelectionStatusDTO dto = new TopicSelectionStatusDTO();
        
        if (proj != null) {
            dto.setEstadoTitulacion(proj.getEstado_titulacion());
            dto.setTipoTemaActual(proj.getTipo_tema_actual());
            dto.setFueraDePlazo(proj.getFuera_de_plazo());
            dto.setDesactivadoPorPlazo(proj.getDesactivado_por_plazo());
            dto.setTieneProcesoTema(proj.getTiene_proceso_tema());
            dto.setTieneSeleccionBanco(proj.getTiene_seleccion_banco());
            dto.setPuedeSeleccionar(proj.getPuede_seleccionar());
            dto.setPuedeProponer(proj.getPuede_proponer());
            dto.setPuedeCambiarTema(proj.getPuede_cambiar_tema());
            dto.setCambiosTemaRealizados(proj.getCambios_tema_realizados());
            dto.setFechaLimiteSeleccion(proj.getFecha_limite_seleccion());
            dto.setMensaje(proj.getMensaje());
        }
        return dto;
    }

    // ==============================================================================
    // 2. SELECCIÓN Y CAMBIO DE TEMA DE BANCO
    // ==============================================================================
    @Override
    @Transactional
    public TopicSelectionResponseDTO procesarSeleccionOCambio(Integer idEstudiante, TopicSelectionRequestDTO request) {
        
        // VALIDACIÓN DE SEGURIDAD
        TopicSelectionStatusDTO estado = getEstado(idEstudiante, request.getIdPeriodo());
        if (!estado.getPuedeSeleccionar() && !estado.getPuedeCambiarTema()) {
            return new TopicSelectionResponseDTO(false, "Bloqueado por reglas del sistema: " + estado.getMensaje());
        }

        Integer idMatricula = processSetupRepository.findIdMatricula(idEstudiante, request.getIdPeriodo());
        if (idMatricula == null) {
            return new TopicSelectionResponseDTO(false, "No se encontró matrícula del estudiante en el período seleccionado.");
        }

        TopicSelectionProjection result = repository.executeTopicSelection(
                idMatricula,
                request.getIdOpcion(),
                request.getIdTema(),
                request.getIdTemaPropuesto(),
                request.getMotivo() == null || request.getMotivo().isBlank() ? "Selección Inicial" : request.getMotivo()
        );

        return new TopicSelectionResponseDTO(result.getExito(), result.getMensaje());
    }

    // ==============================================================================
    // 3. REGISTRO DE NUEVA PROPUESTA
    // ==============================================================================
    @Override
    @Transactional
    public TopicSelectionResponseDTO registrarPropuesta(Integer idEstudiante, Integer idPeriodo, Integer idOpcion, String titulo, String descripcion, String urlDocumento) {
        
        // VALIDACIÓN DE SEGURIDAD
        TopicSelectionStatusDTO estado = getEstado(idEstudiante, idPeriodo);
        if (!estado.getPuedeProponer()) {
            return new TopicSelectionResponseDTO(false, "Bloqueado por reglas del sistema: " + estado.getMensaje());
        }

        Integer idMatricula = processSetupRepository.findIdMatricula(idEstudiante, idPeriodo);
        if (idMatricula == null) {
            return new TopicSelectionResponseDTO(false, "No se encontró matrícula del estudiante en el período seleccionado.");
        }

        TopicSelectionProjection result = repository.registrarPropuesta(
                idMatricula, idOpcion, titulo, descripcion, urlDocumento
        );

        return new TopicSelectionResponseDTO(result.getExito(), result.getMensaje());
    }

    // ==============================================================================
    // 4. ACTUALIZACIÓN DE PROPUESTA EXISTENTE (SOLO SI FUE RECHAZADA)
    // ==============================================================================
    @Override
    @Transactional
    public TopicSelectionResponseDTO actualizarPropuesta(Integer idPropuesta, Integer idEstudiante, Integer idOpcion, String titulo, String descripcion, String urlDocumento) {
        
        // La validación de si la propuesta está rechazada la hace directamente la función de PostgreSQL
        TopicSelectionProjection result = repository.actualizarPropuesta(
                idPropuesta, idOpcion, titulo, descripcion, urlDocumento
        );

        return new TopicSelectionResponseDTO(result.getExito(), result.getMensaje());
    }
}