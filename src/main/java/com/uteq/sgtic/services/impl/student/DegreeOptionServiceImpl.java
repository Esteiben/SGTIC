package com.uteq.sgtic.services.impl.student;

import com.uteq.sgtic.dtos.student.DegreeOptionDTO;
import com.uteq.sgtic.entities.DegreeOption;
import com.uteq.sgtic.repository.General.DegreeOptionRepository;
import com.uteq.sgtic.services.student.IDegreeOptionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DegreeOptionServiceImpl implements IDegreeOptionService {

    private final DegreeOptionRepository degreeOptionRepository;

    @Override
    public List<DegreeOptionDTO> getActiveOptionsByCareer(Integer idCarrera) {
        List<DegreeOption> options = degreeOptionRepository.findActiveByCareerId(idCarrera);
        
        return options.stream()
                .map(option -> new DegreeOptionDTO(
                        option.getIdOption(),
                        option.getName(),
                        option.getDescription()
                ))
                .collect(Collectors.toList());
    }
}