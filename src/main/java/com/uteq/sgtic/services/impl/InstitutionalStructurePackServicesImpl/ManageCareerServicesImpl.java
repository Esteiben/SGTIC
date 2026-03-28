package com.uteq.sgtic.services.impl.InstitutionalStructurePackServicesImpl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uteq.sgtic.services.InstitutionalStructurePackServices.IManageCareerServices;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;

import com.uteq.sgtic.dtos.institutionalstructure.ManageCareerDTO;
import com.uteq.sgtic.repository.InstitutionalStructurePackRepository.ManageCareerRepository;

@Service
@RequiredArgsConstructor

public class ManageCareerServicesImpl implements IManageCareerServices {
    
    private final ManageCareerRepository manageCareerRepository;

    @Override
    @Transactional
    public void updateCareer(ManageCareerDTO dto) {
        try{
            manageCareerRepository.manageCareer(dto.getIdCareer(), dto.getFaculty(), dto.getName(), false);
        } catch(DataIntegrityViolationException | PersistenceException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void toggleCareerStatus(Integer idCareer) {
        try{
            manageCareerRepository.manageCareer(idCareer, null, null, true);
        } catch(DataIntegrityViolationException | PersistenceException e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
