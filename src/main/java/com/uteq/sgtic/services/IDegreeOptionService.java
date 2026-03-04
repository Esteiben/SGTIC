package com.uteq.sgtic.services;

import com.uteq.sgtic.dtos.DegreeOptionDTO;
import java.util.List;

public interface IDegreeOptionService {
    List<DegreeOptionDTO> getActiveOptions();
}