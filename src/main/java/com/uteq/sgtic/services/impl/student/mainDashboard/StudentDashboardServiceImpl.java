package com.uteq.sgtic.services.impl.student.mainDashboard;

import org.springframework.stereotype.Service;

import com.uteq.sgtic.dtos.student.mainDashboard.DashboardStatusDTO;
import com.uteq.sgtic.repository.student.mainDashboard.NStudentDashboardRepository;
import com.uteq.sgtic.repository.student.mainDashboard.NStudentDashboardRepository.DashboardProjection;
import com.uteq.sgtic.services.student.IStudentDashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentDashboardServiceImpl implements IStudentDashboardService {

    private final NStudentDashboardRepository dashboardRepository;

    public DashboardStatusDTO getDashboardStatus(Integer estudianteId, Integer periodoId) {
        
        DashboardProjection d = dashboardRepository.getStudentDashboardStatus(estudianteId, periodoId);

        if (d == null || d.getEsta_matriculado() == null || !d.getEsta_matriculado()) {
            return DashboardStatusDTO.builder().estaMatriculado(false).build();
        }

        return DashboardStatusDTO.builder()
                .estaMatriculado(d.getEsta_matriculado())
                .prerequisitosNivel1(d.getPrerequisitos_nivel1())
                .temaSeleccionado(d.getTema_seleccionado())
                .directorAsignado(d.getDirector_asignado())
                .defensaAnteproyecto(d.getDefensa_anteproyecto())
                .prerequisitosNivel2(d.getPrerequisitos_nivel2())
                .asistenciaTutorias(d.getAsistencia_tutorias())
                .predefensa(d.getPredefensa())
                .defensaFinal(d.getDefensa_final())
                .nombreTema(d.getNombre_tema())
                .nombreDirector(d.getNombre_director())
                .nombreOpcion(d.getNombre_opcion())
                .totalTutorias(d.getTotal_tutorias())
                .build();
    }
}