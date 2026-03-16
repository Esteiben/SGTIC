package com.uteq.sgtic.services.student;

import com.uteq.sgtic.dtos.student.DegreeOptionDTO;

import java.util.List;

public interface IDegreeOptionService {
    List<DegreeOptionDTO> getActiveOptionsByCareer(Integer idCarrera);
}