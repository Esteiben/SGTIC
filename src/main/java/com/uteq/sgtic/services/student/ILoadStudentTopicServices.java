package com.uteq.sgtic.services.student;

import com.uteq.sgtic.dtos.student.LoadStudentTopicsDTO;
import java.util.List;

public interface ILoadStudentTopicServices {
    List<LoadStudentTopicsDTO> getTemasDisponibles(Integer idCarrera, Integer idOpcion);
}