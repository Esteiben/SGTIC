package com.uteq.sgtic.services.impl.requestAccessImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uteq.sgtic.dtos.requestAccess.RequestAccessDTO;
import com.uteq.sgtic.dtos.requestAccess.SelectionItemDTO;
import com.uteq.sgtic.repository.General.NCareerRepository;
import com.uteq.sgtic.repository.General.NFacultyRepository;
import com.uteq.sgtic.repository.requestAccess.RequestAccessRepository;
import com.uteq.sgtic.services.requestAccess.IRequestAccessServices;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestAccessServiceImpl implements IRequestAccessServices {

    private final RequestAccessRepository requestAccessRepository;
    private final NFacultyRepository facultyRepository;
    private final NCareerRepository careerRepository;

    @Override
    public void createRequest(RequestAccessDTO dto) {
        // 1. Validaciones básicas de entrada
        validarDto(dto);

        // 2. Llamar directamente a la función de la base de datos
        RequestAccessRepository.RequestResultProjection result = requestAccessRepository.enviarSolicitud(
                dto.getIdentificacion().trim(),
                dto.getNombres().trim(),
                dto.getApellidos().trim(),
                dto.getCorreo().trim().toLowerCase(),
                dto.getIdCarrera(),
                dto.getIdFacultad()
        );

        // 3. Evaluar la respuesta de PostgreSQL
        if (result == null || !result.getExito()) {
            // Si la base de datos detectó un duplicado o falta de periodo, lanzamos el mensaje exacto
            String errorMsg = result != null ? result.getMensaje() : "Error desconocido al procesar la solicitud.";
            throw new IllegalStateException(errorMsg);
        }
        
        // Si exito es true, la función insertó correctamente y no hacemos nada más.
    }

    @Override
    public List<SelectionItemDTO> getFaculties() {
        // Obtenemos el catálogo de facultades activas
        return facultyRepository.findAllActiveFaculties();
    }

    @Override
    public List<SelectionItemDTO> getCareersByFaculty(Integer idFacultad) {
        // Obtenemos el catálogo de carreras filtradas por facultad
        if (idFacultad == null) {
            throw new IllegalArgumentException("El ID de la facultad es requerido");
        }
        return careerRepository.findCareersByFacultyId(idFacultad);
    }

    private void validarDto(RequestAccessDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        if (dto.getIdentificacion() == null || dto.getIdentificacion().trim().isEmpty()) {
            throw new IllegalArgumentException("La identificación es obligatoria");
        }

        if (dto.getNombres() == null || dto.getNombres().trim().isEmpty()) {
            throw new IllegalArgumentException("Los nombres son obligatorios");
        }

        if (dto.getApellidos() == null || dto.getApellidos().trim().isEmpty()) {
            throw new IllegalArgumentException("Los apellidos son obligatorios");
        }

        if (dto.getCorreo() == null || dto.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        
        if (dto.getIdFacultad() == null) {
            throw new IllegalArgumentException("La facultad es obligatoria");
        }

        if (dto.getIdCarrera() == null) {
            throw new IllegalArgumentException("La carrera es obligatoria");
        }        
    }
}