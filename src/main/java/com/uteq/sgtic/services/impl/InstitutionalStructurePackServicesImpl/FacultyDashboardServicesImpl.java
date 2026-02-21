package com.uteq.sgtic.services.impl.InstitutionalStructurePackServicesImpl;

import com.uteq.sgtic.dtos.institutionalstructure.CareerDisplayDTO;
import com.uteq.sgtic.dtos.institutionalstructure.FacultyDashboardDTO;
import com.uteq.sgtic.entities.Faculty;
import com.uteq.sgtic.repository.InstitutionalStructurePackRepository.FacultyDashboardRepository;
import com.uteq.sgtic.services.InstitutionalStructurePackServices.IFacultyDashboardServices;
import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacultyDashboardServicesImpl implements IFacultyDashboardServices {
    
    private final FacultyDashboardRepository facultyDashboardRepository;

    @Override
    public List<FacultyDashboardDTO> getFacultyDashboardData() {
        List<Faculty> faculties = facultyDashboardRepository.findAll();

        return faculties.stream()
            .map(faculty -> {
                FacultyDashboardDTO dto = new FacultyDashboardDTO();

                dto.setId(faculty.getIdFaculty()); // Asegúrate de que el getter coincida con tu entity
                dto.setName(faculty.getName());
                dto.setSubtitle(faculty.getAcronym());

                // Visuales
                dto.setIcon("school");
                dto.setIconBg("#e3f2fd");
                dto.setIconColor("#1976d2");

                if(faculty.getCareers() != null){
                    List<CareerDisplayDTO> mappedCareers = faculty.getCareers().stream()
                        .map(career -> new CareerDisplayDTO(
                            career.getIdCareer(), // Asegúrate de usar el getter correcto (ej. getId() o getIdCareer())
                            career.getName(),
                            career.getActive() != null ? career.getActive() : true
                        ))
                        .toList();

                    dto.setCareers(mappedCareers);
                    dto.setCareersCount(mappedCareers.size());
                } else {
                    dto.setCareers(List.of());
                    dto.setCareersCount(0);
                }
                 return dto;
            })
            .toList();
    }
}