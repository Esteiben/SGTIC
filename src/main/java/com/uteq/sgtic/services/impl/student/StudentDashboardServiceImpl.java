package com.uteq.sgtic.services.impl.student;

import com.uteq.sgtic.dtos.student.DashboardStatusDTO;
import com.uteq.sgtic.repository.student.StudentDashboardRepository;
import com.uteq.sgtic.services.student.IStudentDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentDashboardServiceImpl implements IStudentDashboardService {

    private final StudentDashboardRepository dashboardRepository;

    @Override
    public DashboardStatusDTO getDashboardStatus(Integer userId) {
        
        StudentDashboardRepository.DashboardProjection p = dashboardRepository.getStudentDashboardStatus(userId);
        
        if (p == null) {
            return new DashboardStatusDTO(false, false, false, false, false, false, null, null);
        }

        return new DashboardStatusDTO(
            p.getTemaSeleccionado(),
            p.getDirectorAsignado(),
            p.getProcesoIniciado(),
            p.getTribunalAsignado(),
            p.getActaEntregada(),
            p.getFinalizado(),
            p.getNombreTema(),
            p.getNombreDirector()
        );
    }
}