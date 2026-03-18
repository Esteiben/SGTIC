package com.uteq.sgtic.services.impl.student;

import com.uteq.sgtic.dtos.student.EnrollmentRequestDTO;
import com.uteq.sgtic.dtos.student.EnrollmentResponseDTO;
import com.uteq.sgtic.entities.AcademicPeriod;
import com.uteq.sgtic.entities.Student;
import com.uteq.sgtic.entities.StudentDegreePeriod;
import com.uteq.sgtic.repository.AcademicPeriodRepository;
import com.uteq.sgtic.repository.student.StudentDegreePeriodRepository;
import com.uteq.sgtic.repository.StudentRepository;
import com.uteq.sgtic.services.student.IStudentDegreePeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentDegreePeriodServiceImpl implements IStudentDegreePeriodService {

    private final StudentDegreePeriodRepository periodRepository;
    private final StudentRepository studentRepository;
    private final AcademicPeriodRepository academicPeriodRepository; // Asegúrate de tenerlo

    @Override
    @Transactional
    public EnrollmentResponseDTO enrollStudent(EnrollmentRequestDTO request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        AcademicPeriod period = academicPeriodRepository.findById(request.getPeriodId())
                .orElseThrow(() -> new RuntimeException("Periodo no encontrado"));

        // REGLA 1: Verificar si ya es GRADUADO
        if ("GRADUADO".equals(student.getDegreeStatus())) {
            throw new RuntimeException("El estudiante ya finalizó su proceso de titulación exitosamente.");
        }

        // REGLA 2: No puede tener dos matrículas EN_CURSO a la vez
        if (periodRepository.existsByStudentIdStudentAndPeriodStatus(student.getIdStudent(), "EN_CURSO")) {
            throw new RuntimeException("El estudiante ya tiene una matrícula en curso.");
        }

        StudentDegreePeriod newEnrollment = new StudentDegreePeriod();
        newEnrollment.setStudent(student);
        newEnrollment.setAcademicPeriod(period);
        newEnrollment.setPeriodStatus("EN_CURSO");
        newEnrollment.setEnrollmentDate(LocalDateTime.now());
        newEnrollment.setApproved(false);

        // REGLA 3: Determinación automática del Nivel y Tipo de Matrícula
        Optional<StudentDegreePeriod> lastPeriodOpt = periodRepository.findLastFinishedPeriodByStudent(student.getIdStudent());

        if (lastPeriodOpt.isEmpty()) {
            // Primera vez absoluto
            newEnrollment.setLevel(1);
            newEnrollment.setEnrollmentType("INICIAL");
            newEnrollment.setPeriodType("DESARROLLO_I");
        } else {
            StudentDegreePeriod lastPeriod = lastPeriodOpt.get();
            newEnrollment.setPreviousRecord(lastPeriod);
            newEnrollment.setEnrollmentType("REMATRICULA");

            if ("APROBADO".equals(lastPeriod.getPeriodStatus())) {
                // Pasó el nivel anterior
                if (lastPeriod.getLevel() == 1) {
                    newEnrollment.setLevel(2);
                    newEnrollment.setPeriodType("DESARROLLO_II");
                } else {
                    throw new RuntimeException("El estudiante ya aprobó el Nivel 2 pero no está graduado. Revisar consistencia.");
                }
            } else {
                // Reprobó, mantiene el nivel
                newEnrollment.setLevel(lastPeriod.getLevel());
                newEnrollment.setPeriodType(lastPeriod.getLevel() == 1 ? "DESARROLLO_I" : "DESARROLLO_II");
            }
        }

        StudentDegreePeriod saved = periodRepository.save(newEnrollment);

        EnrollmentResponseDTO response = new EnrollmentResponseDTO();
        response.setId(saved.getId());
        response.setLevel(saved.getLevel());
        response.setEnrollmentType(saved.getEnrollmentType());
        response.setPeriodStatus(saved.getPeriodStatus());
        response.setEnrollmentDate(saved.getEnrollmentDate());
        response.setMessage("Matrícula creada exitosamente.");

        return response;
    }

    @Override
    @Transactional
    public void closePeriod(Integer enrollmentId, boolean approved, String observations) {
        StudentDegreePeriod enrollment = periodRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada"));

        // Aquí puedes agregar validaciones de Hitos (ej: verificar que entregó todo antes de aprobar)

        enrollment.setPeriodStatus(approved ? "APROBADO" : "REPROBADO");
        enrollment.setApproved(approved);
        enrollment.setClosingDate(LocalDateTime.now());
        enrollment.setApprovalDate(approved ? LocalDateTime.now() : null);
        enrollment.setObservations(observations);

        periodRepository.save(enrollment);
    }
}