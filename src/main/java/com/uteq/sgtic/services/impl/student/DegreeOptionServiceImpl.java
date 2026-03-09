package com.uteq.sgtic.services.impl.student;

import com.uteq.sgtic.dtos.student.DegreeOptionDTO;
import com.uteq.sgtic.repository.General.DegreeOptionRepository;
import com.uteq.sgtic.services.student.IDegreeOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("studentDegreeOptionService")
@RequiredArgsConstructor
public class DegreeOptionServiceImpl implements IDegreeOptionService {

    private final DegreeOptionRepository degreeOptionRepository;

    @Override
    public List<DegreeOptionDTO> getActiveOptionsByCareer(Integer idCarrera) {
        return degreeOptionRepository.findActiveByCareerId(idCarrera)
                .stream()
                .map(p -> new DegreeOptionDTO(
                        p.getIdOption(),
                        p.getNombre(),
                        p.getDescripcion()
                ))
                .toList();
    }
}