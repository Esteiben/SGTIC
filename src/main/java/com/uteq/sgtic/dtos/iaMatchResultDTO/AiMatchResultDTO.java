package com.uteq.sgtic.dtos.iaMatchResultDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiMatchResultDTO {
    private Integer idDocente;
    private Integer matchScore;
    private String razonIA;
}