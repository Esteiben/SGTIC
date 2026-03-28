package com.uteq.sgtic.services.student.selectTopic;

import java.util.List;

import com.uteq.sgtic.dtos.student.selectTopic.DegreeOptionDTO;
import com.uteq.sgtic.dtos.student.selectTopic.StudentProposalSummaryDTO;
import com.uteq.sgtic.dtos.student.selectTopic.TemaDTO;
import com.uteq.sgtic.repository.student.selectTopic.ProcessSetupRepository;

public interface IProcessSetupService {
    List<DegreeOptionDTO> getOptionsByStudentPeriod(Integer idPeriodo, Integer idEstudiante);
    List<TemaDTO> getTemasDisponibles(Integer idPeriodo, Integer idEstudiante, Integer idOpcion);
    List<StudentProposalSummaryDTO> getStudentProposals(Integer idPeriodo, Integer idEstudiante);
    Integer getIdEstudianteByUsername(String username);

    //Historial
    List<ProcessSetupRepository.TopicSelectionHistoryProjection> getTopicSelectionHistory(Integer idPeriodo, Integer idEstudiante);
    List<ProcessSetupRepository.StudentProposalHistoryProjection> getStudentProposalHistory(Integer idPropuesta);
}