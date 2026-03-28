package com.uteq.sgtic.dtos.tutorship;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TutorshipResponseDTO {
    private Integer idTutoring;
    private String studentName;
    private String thesisTitle;
    private LocalDate date;
    private String type;
    private String modality;
    private String locationLink;
    private String status;
    private String observations;
}
