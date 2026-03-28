package com.uteq.sgtic.services.InstitutionalStructurePackServices;

import java.util.List;
import com.uteq.sgtic.dtos.institutionalstructure.FacultyDashboardDTO;

public interface IFacultyDashboardServices {
    List<FacultyDashboardDTO> getFacultyDashboardData();
}
