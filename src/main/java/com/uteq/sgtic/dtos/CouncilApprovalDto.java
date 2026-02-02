package com.uteq.sgtic.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CouncilApprovalDto {
    private Integer idApproval;
    private Integer idProposal;
    private Boolean approved;
    private LocalDate date;
    private String observations;
}
