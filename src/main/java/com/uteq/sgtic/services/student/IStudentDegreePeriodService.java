package com.uteq.sgtic.services.student;

import com.uteq.sgtic.dtos.student.EnrollmentRequestDTO;
import com.uteq.sgtic.dtos.student.EnrollmentResponseDTO;

public interface IStudentDegreePeriodService {
    EnrollmentResponseDTO enrollStudent(EnrollmentRequestDTO request);
    void closePeriod(Integer enrollmentId, boolean approved, String observations);
}   