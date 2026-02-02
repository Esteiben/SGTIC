package com.uteq.sgtic.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DirectorAssignmentDto {
    private Integer idAssignment;
    private Integer idProposal;
    private Integer idDocente;
    private String decision;
    private String observations;
    private LocalDate decisionDate;
}
