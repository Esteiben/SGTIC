package com.uteq.sgtic.projections;

import java.time.LocalDate;

public interface RequestManagementCoordinatorProjection {
    Integer getIdSolicitud();
    String getIdentificacion();
    String getNombres();
    String getApellidos();
    String getCorreo();
    String getCarrera();
    String getFacultad();
    String getPeriodo();
    LocalDate getFechaEnvio();
    String getEstado();
    String getObservaciones();
    LocalDate getFechaRespuesta();
}