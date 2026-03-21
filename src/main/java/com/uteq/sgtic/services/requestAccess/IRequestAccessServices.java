package com.uteq.sgtic.services.requestAccess;

import java.util.List;

import com.uteq.sgtic.dtos.requestAccess.RequestAccessDTO;
import com.uteq.sgtic.dtos.requestAccess.SelectionItemDTO;

public interface IRequestAccessServices {
    void createRequest(RequestAccessDTO dto);
    List<SelectionItemDTO> getFaculties();
    List<SelectionItemDTO> getCareersByFaculty(Integer idFacultad);
}