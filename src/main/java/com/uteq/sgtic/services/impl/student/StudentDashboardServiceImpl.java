package com.uteq.sgtic.services.impl.student;

import com.uteq.sgtic.dtos.student.DashboardStatusDTO;
import com.uteq.sgtic.entities.StudentDegreePeriod;
import com.uteq.sgtic.entities.WorkProposal;
import com.uteq.sgtic.entities.DirectorAssignment;
import com.uteq.sgtic.entities.DegreePeriodMilestone;
import com.uteq.sgtic.repository.student.StudentDegreePeriodRepository;
import com.uteq.sgtic.repository.student.WorkProposalRepository;
import com.uteq.sgtic.repository.student.DirectorAssignmentRepository;
import com.uteq.sgtic.repository.tutorship.WorkTutoringRepository;
import com.uteq.sgtic.repository.student.DegreePeriodMilestoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentDashboardServiceImpl {

    private final StudentDegreePeriodRepository periodRepository;
    private final WorkProposalRepository proposalRepository;
    
    // Repositorios inyectados para verificar el flujo real
    private final DirectorAssignmentRepository directorAssignmentRepository;
    private final WorkTutoringRepository tutoringRepository;
    private final DegreePeriodMilestoneRepository milestoneRepository;

    public DashboardStatusDTO getDashboardStatus(Integer estudianteId, Integer periodoId) {
        
        Optional<StudentDegreePeriod> matriculaOpt = periodRepository.findByStudentIdStudentAndAcademicPeriodIdPeriod(estudianteId, periodoId);

        if (matriculaOpt.isEmpty()) {
            return DashboardStatusDTO.builder().estaMatriculado(false).build();
        }

        StudentDegreePeriod matricula = matriculaOpt.get();

        DashboardStatusDTO.DashboardStatusDTOBuilder builder = DashboardStatusDTO.builder()
                .estaMatriculado(true)
                .prerequisitosNivel1(true); // Se asume true al estar matriculado

        // 1. VERIFICAR TEMA Y OPCIÓN
        Optional<WorkProposal> propuestaOpt = proposalRepository.findFirstByStudentIdStudentAndAcademicPeriodIdPeriodOrderByIdProposalDesc(estudianteId, periodoId);

        if (propuestaOpt.isPresent()) {
            WorkProposal propuesta = propuestaOpt.get();
            
            if (propuesta.getTopic() != null) {
                builder.nombreTema(propuesta.getTopic().getTitle());
                builder.nombreOpcion(propuesta.getTopic().getDegreeOption() != null ? propuesta.getTopic().getDegreeOption().getName() : "Sin opción");
                builder.temaSeleccionado(true);
            } else if (propuesta.getProposedTopic() != null) {
                builder.nombreTema(propuesta.getProposedTopic().getTitle());
                builder.nombreOpcion(propuesta.getProposedTopic().getDegreeOption() != null ? propuesta.getProposedTopic().getDegreeOption().getName() : "Sin opción");
                builder.temaSeleccionado("aprobado".equalsIgnoreCase(propuesta.getProposedTopic().getStatus()));
            }

            // 2. VERIFICAR DIRECTOR (Solo si hay propuesta)
            Optional<DirectorAssignment> asignacionOpt = directorAssignmentRepository
                    .findByWorkProposalIdProposalAndResponse(propuesta.getIdProposal(), "aceptado");

            if (asignacionOpt.isPresent()) {
                builder.directorAsignado(true);
                builder.nombreDirector(asignacionOpt.get().getTeacher().getUser().getFirstName() + " " + asignacionOpt.get().getTeacher().getUser().getLastName());
            }
        }

        // 3. VERIFICAR TUTORÍAS / REUNIONES (Aislando por el periodo exacto como pediste)
        long tutoriasCumplidas = tutoringRepository.countByStudentIdStudentAndDegreeWorkAcademicPeriodIdPeriodAndRegisteredTrue(estudianteId, periodoId);
        int tutoriasMinimasRequeridas = matricula.getAcademicPeriod().getMinAdvances() != null ? matricula.getAcademicPeriod().getMinAdvances() : 5; // Asumimos 5 por defecto
        
        builder.totalTutorias((int) tutoriasCumplidas);
        builder.reunionesMinimas(tutoriasCumplidas >= tutoriasMinimasRequeridas);

        // 4. VERIFICAR HITOS (Anteproyecto, Predefensa, Defensa Final)
        // Buscamos en la tabla HitoPeriodoTitulacion amarrada a esta matrícula exacta
        Optional<DegreePeriodMilestone> anteproyecto = milestoneRepository.findByStudentDegreePeriodIdAndMilestoneType(matricula.getId(), "ANTEPROYECTO");
        builder.defensaAnteproyecto(anteproyecto.isPresent() && "APROBADO".equals(anteproyecto.get().getStatus()));

        // Lógica Exclusiva para Nivel 2
        if (matricula.getLevel() == 2) {
            builder.prerequisitosNivel2(true); // Al estar matriculado en nivel 2, cumple
            
            // Asistencia a tutorías de Nivel 2
            builder.asistenciaTutorias(tutoriasCumplidas >= tutoriasMinimasRequeridas);

            Optional<DegreePeriodMilestone> predefensa = milestoneRepository.findByStudentDegreePeriodIdAndMilestoneType(matricula.getId(), "PREDEFENSA");
            builder.predefensa(predefensa.isPresent() && "APROBADO".equals(predefensa.get().getStatus()));

            Optional<DegreePeriodMilestone> defensaFinal = milestoneRepository.findByStudentDegreePeriodIdAndMilestoneType(matricula.getId(), "DEFENSA_FINAL");
            builder.defensaFinal(defensaFinal.isPresent() && "APROBADO".equals(defensaFinal.get().getStatus()));
            
            // Si está en Nivel 2, todo lo del Nivel 1 DEBE estar visualmente en true
            builder.temaSeleccionado(true);
            builder.directorAsignado(true);
            builder.reunionesMinimas(true);
            builder.defensaAnteproyecto(true);
        }

        return builder.build();
    }
}