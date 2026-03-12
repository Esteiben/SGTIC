package com.uteq.sgtic.services.student;

import com.uteq.sgtic.dtos.student.RegisterProposalStudentTopicDTO;
import com.uteq.sgtic.dtos.student.RegisterProposalStudentTopicResponseDTO;

public interface IRegisterProposalStudentTopicServices {
    RegisterProposalStudentTopicResponseDTO registrarPropuestaTema(RegisterProposalStudentTopicDTO dto) throws Exception;
}