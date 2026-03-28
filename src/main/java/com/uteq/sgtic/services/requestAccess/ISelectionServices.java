package com.uteq.sgtic.services.requestAccess;

import com.uteq.sgtic.dtos.requestAccess.SelectionItemDTO;
import java.util.List;
public interface ISelectionServices {
    List<SelectionItemDTO> getActiveFaculties();
    List<SelectionItemDTO> getActiveCareersByFaculty(Integer facultyId);
}
