package com.uteq.sgtic.services.impl.InstitutionalStructurePackServicesImpl;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.uteq.sgtic.dtos.institutionalstructure.CareerCreateDTO;
import com.uteq.sgtic.repository.InstitutionalStructurePackRepository.CareerCreateRepository;
import com.uteq.sgtic.services.InstitutionalStructurePackServices.ICareerCreateServices;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class CareerCreateServicesImpl implements ICareerCreateServices {
    
    private final CareerCreateRepository careerCreateRepository;

    @Override
    @Transactional

    public void createCareer(CareerCreateDTO dto){
        try{
            careerCreateRepository.createCareer(dto.getFaculty(), dto.getName());
        } catch(DataIntegrityViolationException | PersistenceException e) {
            // String mensajeLimpio = extractDbErrorMessage(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    //Esto es para limpiar los errres. Revisarlos después
    // private String extractDbErrorMessage(String rawMessage) {
    //     if (rawMessage == null) return "Error desconocido";

    //     if (rawMessage.contains("ERROR:")) {
    //         int start = rawMessage.indexOf("ERROR:") + 6;
    //         int end = rawMessage.indexOf("Where:");
            
    //         if (end == -1) {
    //             return rawMessage.substring(start).trim();
    //         }
            
    //         return rawMessage.substring(start, end).trim();
    //     }
        
    //     return rawMessage;
    // }
}
