package com.uteq.sgtic.services.impl.topicProposedServiceImpl;

import com.uteq.sgtic.repository.topicProposedRepository.TopicProposedRepository;
import com.uteq.sgtic.dtos.pendingProposalDTO.PendingProposalDTO;
import com.uteq.sgtic.services.ITopicProposedService; // Importa la interfaz
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicProposedServiceImpl implements ITopicProposedService {

    private final TopicProposedRepository repository;

    @Override
    public List<PendingProposalDTO> obtenerPropuestasPendientes(Integer idCoordinador) {
        List<Object[]> resultados = repository.listarPropuestasPendientes(idCoordinador);
        return resultados.stream().map(row -> PendingProposalDTO.builder()
                .idTemaPropuesto((Integer) row[0])
                .nombreEstudiante((String) row[1])
                .identificacionEstudiante((String) row[2])
                .carreraNombre((String) row[3])
                .titulo((String) row[4])
                .descripcion((String) row[5])
                .fechaEnvio((String) row[6])
                .urlDocumento((String) row[7])
                .nombreModalidad((String) row[8])
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public void procesarPropuesta(Integer idPropuesta, String estado) {
        repository.responderPropuesta(idPropuesta, estado);
    }
}