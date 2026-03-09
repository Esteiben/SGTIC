package com.uteq.sgtic.services.student;

import com.uteq.sgtic.dtos.student.ProposalTopicStudentDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IProposalTopicStudent {
    void crearPropuesta(Integer idEstudiante, ProposalTopicStudentDTO dto, MultipartFile archivo);
}