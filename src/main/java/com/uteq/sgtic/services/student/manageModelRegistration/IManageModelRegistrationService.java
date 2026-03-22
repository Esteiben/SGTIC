package com.uteq.sgtic.services.student.manageModelRegistration;

import com.uteq.sgtic.dtos.student.manageModelRegistration.EnrollmentResponseDTO;

public interface IManageModelRegistrationService {
    EnrollmentResponseDTO autoEnrollStudent(Integer studentId);
}