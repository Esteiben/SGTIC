package com.uteq.sgtic.dtos;

import lombok.Data;

@Data
public class TeacherResponseDTO {

    private Integer idTeacher;
    private String fullName;
    private String email;
    private String degree;
    private Boolean isResearcher;
    private String contract;
    private Integer workload;
}