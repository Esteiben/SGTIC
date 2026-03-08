package com.uteq.sgtic.services.student;

import com.uteq.sgtic.dtos.student.SaveTopicSelectionRequestDTO;
import com.uteq.sgtic.dtos.student.SaveTopicSelectionResponseDTO;

public interface IStudentTopicSelectionService {
    SaveTopicSelectionResponseDTO saveSelection(
            Integer userId,
            Integer idCarrera,
            SaveTopicSelectionRequestDTO request
    );
}