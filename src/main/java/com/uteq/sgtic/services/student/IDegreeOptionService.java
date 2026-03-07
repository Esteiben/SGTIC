package com.uteq.sgtic.services.student;

import java.util.List;

import com.uteq.sgtic.dtos.student.DegreeOptionDTO;

public interface IDegreeOptionService {
    List<DegreeOptionDTO> getActiveOptionsByCareer(Integer idCarrera);
}