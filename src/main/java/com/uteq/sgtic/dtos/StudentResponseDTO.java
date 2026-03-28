package com.uteq.sgtic.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentResponseDTO {
    private Integer idStudent;
    private Integer idUser;
    private Integer idCareer;
    private String status;
    private LocalDate entryDate;
}
