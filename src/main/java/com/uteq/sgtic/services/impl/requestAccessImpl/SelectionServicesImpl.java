package com.uteq.sgtic.services.impl.requestAccessImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.uteq.sgtic.services.requestAccess.ISelectionServices;
import com.uteq.sgtic.dtos.requestAccess.SelectionItemDTO;
import com.uteq.sgtic.repository.General.ReadAllFacultyActiveRepository;
import com.uteq.sgtic.repository.General.ReadByFacultyAllCareerActiveRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SelectionServicesImpl implements ISelectionServices {
   private final ReadAllFacultyActiveRepository readAllFacultyActiveRepository;
    private final ReadByFacultyAllCareerActiveRepository readByFacultyAllCareerActiveRepository;

    @Override
    public List<SelectionItemDTO> getActiveFaculties() {
        return readAllFacultyActiveRepository.findByActiveTrue().stream()
                .map(f -> new SelectionItemDTO(f.getIdFaculty(), f.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SelectionItemDTO> getActiveCareersByFaculty(Integer facultyId) {
        return readByFacultyAllCareerActiveRepository.findByFaculty_IdFacultyAndActiveTrue(facultyId).stream()
                .map(c -> new SelectionItemDTO(c.getIdCareer(), c.getName()))
                .collect(Collectors.toList());
    }
}
