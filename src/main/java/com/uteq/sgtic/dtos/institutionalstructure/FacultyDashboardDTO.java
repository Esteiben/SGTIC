package com.uteq.sgtic.dtos.institutionalstructure;

import lombok.Data;
import java.util.List;

@Data
public class FacultyDashboardDTO {
    private Integer id;
    private String name;
    private String subtitle; // Esto es para las siglas (FCI, FCE)
    private Integer careersCount;
    
    private List<CareerDisplayDTO> careers;  

    // Datos visuales
    private String icon = "school"; 
    private String iconBg = "#e3f2fd";
    private String iconColor = "#1976d2";
}