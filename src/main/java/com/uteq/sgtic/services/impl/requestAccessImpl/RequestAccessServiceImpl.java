package com.uteq.sgtic.services.impl.requestAccessImpl;

import com.uteq.sgtic.dtos.requestAccess.RequestAccessDTO;
import com.uteq.sgtic.entities.AcademicPeriod;
import com.uteq.sgtic.entities.AdmissionRequest;
import com.uteq.sgtic.entities.Career;
import com.uteq.sgtic.repository.AcademicPeriodRepository;
import com.uteq.sgtic.repository.CareerRepository;
import com.uteq.sgtic.repository.requestAccess.RequestAccessRepository;
import com.uteq.sgtic.services.requestAccess.IRequestAccessServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestAccessServiceImpl implements IRequestAccessServices {

    private final RequestAccessRepository requestAccessRepository;
    private final CareerRepository careerRepository;
    private final AcademicPeriodRepository academicPeriodRepository;

    @Override
    public void createRequest(RequestAccessDTO dto) {
        validarDto(dto);

        List<String> estadosBloqueantes = List.of("pendiente", "aprobada");

        boolean existePorIdentificacion = requestAccessRepository
                .existsByIdentificationAndAcademicPeriod_IdPeriodAndStatusIn(
                        dto.getIdentificacion().trim(),
                        dto.getIdPeriodo(),
                        estadosBloqueantes
                );

        if (existePorIdentificacion) {
            throw new IllegalStateException(
                    "Ya existe una solicitud pendiente o aprobada para este estudiante en ese período"
            );
        }

        boolean existePorCorreo = requestAccessRepository
                .existsByEmailAndAcademicPeriod_IdPeriodAndStatusIn(
                        dto.getCorreo().trim().toLowerCase(),
                        dto.getIdPeriodo(),
                        estadosBloqueantes
                );

        if (existePorCorreo) {
            throw new IllegalStateException(
                    "Ya existe una solicitud registrada con ese correo para ese período"
            );
        }

        Career career = careerRepository.findById(dto.getIdCarrera())
                .orElseThrow(() -> new IllegalArgumentException("La carrera no existe"));

        AcademicPeriod academicPeriod = academicPeriodRepository.findById(dto.getIdPeriodo())
                .orElseThrow(() -> new IllegalArgumentException("El período no existe"));

        AdmissionRequest request = new AdmissionRequest();
        request.setIdentification(dto.getIdentificacion().trim());
        request.setFirstName(dto.getNombres().trim());
        request.setLastName(dto.getApellidos().trim());
        request.setEmail(dto.getCorreo().trim().toLowerCase());
        request.setStatus("pendiente");
        request.setObservations(null);
        request.setResponseDate(null);
        request.setCareer(career);
        request.setAcademicPeriod(academicPeriod);

        requestAccessRepository.save(request);
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

        if (dto.getIdCarrera() == null) {
            throw new IllegalArgumentException("La carrera es obligatoria");
        }

        if (dto.getIdPeriodo() == null) {
            throw new IllegalArgumentException("El período es obligatorio");
        }
    }
}