package com.uteq.sgtic.services.student;

import com.uteq.sgtic.dtos.student.DashboardStatusDTO;

public interface IStudentDashboardService {
    DashboardStatusDTO getDashboardStatus(Integer userId, Integer periodoId);
}