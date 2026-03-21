package com.uteq.sgtic.dtos.student.selectTopic;

import lombok.Data;

@Data
public class StudentProposalSummaryDTO {
    private Integer idPropuesta;
    private Integer idTemaPropuesto;
    private String titulo;
    private String descripcion;
    private String estadoPropuesta;
    private String estadoTema;
    private String feedbackDocente;
    private String urlDocumento;
    private Integer idOpcion;
    private String nombreOpcion;
    private Integer numeroVersion;
    private Integer totalVersiones;
    private String fechaEnvio;
    private String fechaUltimaActualizacion;
    private Boolean editable;
}
