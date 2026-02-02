package com.uteq.sgtic.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WorkProposalDto {
    private Integer idProposal;
    private Integer idStudent;
    private String title;
    private String description;
    private LocalDate registrationDate;
    private String status;
}
