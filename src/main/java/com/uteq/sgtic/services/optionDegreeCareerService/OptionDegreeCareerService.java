package com.uteq.sgtic.services.optionDegreeCareerService;

import com.uteq.sgtic.projections.optionCareer.OptionCareerProjection;
import com.uteq.sgtic.repository.optionDegreeCareer.OptionDegreeCareerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionDegreeCareerService {

    private final OptionDegreeCareerRepository repository;

    @Transactional(readOnly = true)
    public List<OptionCareerProjection> getOptionsForCoordinator(Integer idUsuario) {
        return repository.getOptionsByCareer(idUsuario);
    }

    @Transactional
    public void toggleOption(Integer idUsuario, Integer idOpcion, Boolean seleccionado) {
        repository.toggleOptionCareer(idUsuario, idOpcion, seleccionado);
    }
}