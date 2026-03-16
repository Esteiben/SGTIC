package com.uteq.sgtic.dtos.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterProposalStudentTopicDTO {

    @NotNull(message = "El idUsuario es obligatorio")
    private Integer idUsuario;

    @NotNull(message = "El idOpcion es obligatorio")
    private Integer idOpcion;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private MultipartFile documento;
}