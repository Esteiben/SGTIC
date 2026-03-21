package com.uteq.sgtic.dtos.student.selectTopic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicSelectionResponseDTO {
    private Boolean exito;
    private String mensaje;
}