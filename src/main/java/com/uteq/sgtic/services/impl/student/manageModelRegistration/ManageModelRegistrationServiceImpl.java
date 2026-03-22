package com.uteq.sgtic.services.impl.student.manageModelRegistration;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.uteq.sgtic.dtos.student.manageModelRegistration.EnrollmentResponseDTO;
import com.uteq.sgtic.repository.student.manageModelRegistration.ManageModelRegistrationRepository;
import com.uteq.sgtic.services.student.manageModelRegistration.IManageModelRegistrationService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ManageModelRegistrationServiceImpl implements IManageModelRegistrationService {

    private final ManageModelRegistrationRepository registrationRepository;

    @Override
    public EnrollmentResponseDTO autoEnrollStudent(Integer studentId) {
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("ID de estudiante inválido");
        }

        ManageModelRegistrationRepository.EnrollmentProjection result = 
                registrationRepository.processAutoEnrollment(studentId);

        if (result == null) {
            throw new IllegalStateException("Error al procesar la matrícula desde la base de datos.");
        }

        if (!result.getExito()) {
            // Si retorna exito=false es porque no hay periodo activo
            throw new IllegalStateException(result.getMensaje());
        }

        // Mapeamos la proyección a nuestro DTO
        return new EnrollmentResponseDTO(
                result.getExito(),
                result.getId_matricula(),
                result.getNivel_matriculado(),
                result.getMensaje()
        );
    }
}