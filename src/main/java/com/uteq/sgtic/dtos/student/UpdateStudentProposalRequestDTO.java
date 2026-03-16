package com.uteq.sgtic.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentProposalRequestDTO {
    private Integer idOpcion;
    private String titulo;
    private String descripcion;
    private MultipartFile documento;
}