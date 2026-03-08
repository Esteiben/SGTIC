package com.uteq.sgtic.services.impl.student;

import com.uteq.sgtic.dtos.student.LoadStudentTopicsDTO;
import com.uteq.sgtic.repository.student.LoadStudentTopicsRepository;
import com.uteq.sgtic.services.student.ILoadStudentTopicServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadStudentTopicServicesImpl implements ILoadStudentTopicServices {

    private final LoadStudentTopicsRepository repository;

    @Override
    public List<LoadStudentTopicsDTO> getTemasDisponibles(Integer idCarrera, Integer idOpcion) {
        return repository.findTemasDisponibles(idCarrera, idOpcion)
                .stream()
                .map(row -> new LoadStudentTopicsDTO(
                        row.getIdTema(),
                        row.getTitulo(),
                        row.getDescripcion(),
                        row.getIdCarrera(),
                        row.getIdOpcion(),
                        row.getNombreOpcion()
                ))
                .toList();
    }
}