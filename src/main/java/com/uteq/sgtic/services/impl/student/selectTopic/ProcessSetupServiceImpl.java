package com.uteq.sgtic.services.impl.student.selectTopic;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteq.sgtic.dtos.student.selectTopic.DegreeOptionDTO;
import com.uteq.sgtic.dtos.student.selectTopic.StudentProposalSummaryDTO;
import com.uteq.sgtic.dtos.student.selectTopic.TemaDTO;
import com.uteq.sgtic.repository.student.selectTopic.ProcessSetupRepository;
import com.uteq.sgtic.services.student.selectTopic.IProcessSetupService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessSetupServiceImpl implements IProcessSetupService {

    private final ProcessSetupRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public List<DegreeOptionDTO> getOptionsByStudentPeriod(Integer idPeriodo, Integer idEstudiante) {
        return repository.getOptionsByStudentPeriod(idEstudiante, idPeriodo)
                .stream()
                .map(map -> objectMapper.convertValue(map, DegreeOptionDTO.class))
                .toList();
    }

    @Override
    public List<TemaDTO> getTemasDisponibles(Integer idPeriodo, Integer idEstudiante, Integer idOpcion) {
        return repository.getTemasDisponibles(idEstudiante, idPeriodo, idOpcion)
                .stream()
                .map(map -> objectMapper.convertValue(map, TemaDTO.class))
                .toList();
    }

    @Override
    public List<StudentProposalSummaryDTO> getStudentProposals(Integer idPeriodo, Integer idEstudiante) {
        return repository.getStudentProposals(idPeriodo, idEstudiante)
                .stream()
                .map(map -> objectMapper.convertValue(map, StudentProposalSummaryDTO.class))
                .toList();
    }

    @Override
    public Integer getIdEstudianteByUsername(String username) {
        return repository.findIdEstudianteByUsername(username);
    }

    @Override
    public List<ProcessSetupRepository.TopicSelectionHistoryProjection> getTopicSelectionHistory(Integer idPeriodo, Integer idEstudiante) {
        return repository.getTopicSelectionHistory(idEstudiante, idPeriodo);
    }

    @Override
    public List<ProcessSetupRepository.StudentProposalHistoryProjection> getStudentProposalHistory(Integer idPropuesta) {
        return repository.getStudentProposalHistory(idPropuesta);
    }
}