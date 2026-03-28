package com.uteq.sgtic.services.impl;

import com.uteq.sgtic.dtos.DegreeOptionDTO;
import com.uteq.sgtic.entities.DegreeOption;
import com.uteq.sgtic.repository.General.DegreeOptionRepository;
import com.uteq.sgtic.services.IDegreeOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DegreeOptionGeneralServiceImpl implements IDegreeOptionService {

    private final DegreeOptionRepository degreeOptionRepository;

//    @Override
//    @Transactional(readOnly = true)
//    public List<DegreeOptionDTO> getActiveOptions() {
//        return degreeOptionRepository.findAllActiveFromView().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }

    @Override
    @Transactional(readOnly = true)
    public List<DegreeOptionDTO> getAllOptions() {
        return degreeOptionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DegreeOptionDTO getById(Integer id) {
        return degreeOptionRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Opción no encontrada"));
    }

    @Override
    @Transactional
    public DegreeOptionDTO save(DegreeOptionDTO dto) {
        DegreeOption entity;

        if (dto.getIdOption() != null) {
            entity = degreeOptionRepository.findById(dto.getIdOption())
                    .orElseThrow(() -> new RuntimeException("No existe el registro"));
        } else {
            entity = new DegreeOption();
            entity.setCreatedAt(LocalDate.now());
        }

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.getActive() != null ? dto.getActive() : true);
        entity.setIconName(dto.getIconName() != null ? dto.getIconName() : "school");

        return convertToDTO(degreeOptionRepository.save(entity));
    }

    @Override
    @Transactional
    public void toggleStatus(Integer id, Boolean status) {
        DegreeOption entity = degreeOptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID no encontrado"));
        entity.setActive(status);
        degreeOptionRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        degreeOptionRepository.deleteById(id);
    }

    private DegreeOptionDTO convertToDTO(DegreeOption option) {
        return new DegreeOptionDTO(
                option.getIdOption(),
                option.getName(),
                option.getDescription(),
                option.getActive(),
                option.getIconName(),
                option.getCreatedAt()
        );
    }
}