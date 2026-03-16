package com.uteq.sgtic.services.student;

import com.uteq.sgtic.dtos.student.*;

import java.util.List;

public interface IStudentTopicSelectionService {

    SaveTopicSelectionResponseDTO saveSelection(
            Integer userId,
            Integer idCarrera,
            SaveTopicSelectionRequestDTO request
    );

    RegisterProposalStudentTopicResponseDTO registerProposal(
            Integer userId,
            Integer idCarrera,
            RegisterProposalStudentTopicRequestDTO request
    );

    TopicSelectionStatusDTO getSelectionStatus(
            Integer userId,
            Integer idPeriodo
    );

    List<StudentProposalSummaryDTO> getStudentProposals(
            Integer userId,
            Integer idPeriodo
    );

    List<StudentProposalHistoryItemDTO> getStudentProposalHistory(
            Integer userId,
            Integer idPropuesta
    );

    UpdateStudentProposalResponseDTO updateStudentProposal(
            Integer userId,
            Integer idPropuesta,
            UpdateStudentProposalRequestDTO request
    );
}