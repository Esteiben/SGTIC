package com.uteq.sgtic.dtos.tutorship;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TutorshipRequestDTO {
    private Integer idWork;
    private LocalDate date;
    private String type;
    private String modality;
    private String locationLink;
    private String observations;
}
