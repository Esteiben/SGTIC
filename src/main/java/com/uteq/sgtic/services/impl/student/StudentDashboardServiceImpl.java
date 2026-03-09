package com.uteq.sgtic.services.impl.student;

import com.uteq.sgtic.dtos.student.DashboardStatusDTO;
import com.uteq.sgtic.repository.student.StudentDashboardRepository;
import com.uteq.sgtic.services.student.IStudentDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentDashboardServiceImpl implements IStudentDashboardService {

    private final StudentDashboardRepository dashboardRepository;

    @Override
    public DashboardStatusDTO getDashboardStatus(Integer userId, Integer periodoId) {
        StudentDashboardRepository.DashboardProjection p =
                dashboardRepository.getStudentDashboardStatus(userId, periodoId);

        if (p == null) {
            return new DashboardStatusDTO(
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    null,
                    null,
                    null,
                    0
            );
        }

        return new DashboardStatusDTO(
                Boolean.TRUE.equals(p.getPrerequisitosNivel1()),
                Boolean.TRUE.equals(p.getTemaSeleccionado()),
                Boolean.TRUE.equals(p.getDirectorAsignado()),
                Boolean.TRUE.equals(p.getReunionesMinimas()),
                Boolean.TRUE.equals(p.getDefensaAnteproyecto()),
                Boolean.TRUE.equals(p.getPrerequisitosNivel2()),
                Boolean.TRUE.equals(p.getAsistenciaTutorias()),
                Boolean.TRUE.equals(p.getPredefensa()),
                Boolean.TRUE.equals(p.getDefensaFinal()),
                p.getNombreTema(),
                p.getNombreDirector(),
                p.getNombreOpcion(),
                p.getTotalTutorias() != null ? p.getTotalTutorias() : 0
        );
    }
}