package com.uteq.sgtic.services.InstitutionalStructurePackServices;

import com.uteq.sgtic.dtos.institutionalstructure.ManageCareerDTO;;

public interface IManageCareerServices {
    void updateCareer(ManageCareerDTO manageCareerDTO);
    void toggleCareerStatus(Integer idCareer);
}
