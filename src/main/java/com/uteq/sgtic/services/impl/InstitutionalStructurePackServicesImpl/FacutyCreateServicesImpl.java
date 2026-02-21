package com.uteq.sgtic.services.impl.InstitutionalStructurePackServicesImpl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.uteq.sgtic.dtos.institutionalstructure.FacultyCreateDTO;
import com.uteq.sgtic.repository.InstitutionalStructurePackRepository.FacultyCreateRepository;
import com.uteq.sgtic.services.InstitutionalStructurePackServices.IFacultyCreateServices;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor

public class FacutyCreateServicesImpl implements IFacultyCreateServices {
    
    private final FacultyCreateRepository facultyCreateRepository;

    @Override
    @Transactional

    public void createFaculty(FacultyCreateDTO dto){
        try{
            facultyCreateRepository.createFaculty(dto.getName(), dto.getAcronym());
        }catch (DataIntegrityViolationException | PersistenceException e){
            throw new RuntimeException("Error al crear la facaultad:" + e.getMessage());
        }
    }
}
