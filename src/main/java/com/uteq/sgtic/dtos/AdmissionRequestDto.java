package com.uteq.sgtic.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AdmissionRequestDto {
    private Integer idRequest;
    private String identification;
    private String firstName;
    private String lastName;
    private String email;
    private Integer idCareer;
    private LocalDate sentDate;
    private String status;
    private String observations;
}
