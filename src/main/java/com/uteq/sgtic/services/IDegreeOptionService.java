package com.uteq.sgtic.services;

import com.uteq.sgtic.dtos.DegreeOptionDTO;
import java.util.List;

public interface IDegreeOptionService {
    //List<DegreeOptionDTO> getActiveOptions();
    List<DegreeOptionDTO> getAllOptions();
    DegreeOptionDTO getById(Integer id);
    DegreeOptionDTO save(DegreeOptionDTO dto);
    void toggleStatus(Integer id, Boolean status);
    void delete(Integer id);
}