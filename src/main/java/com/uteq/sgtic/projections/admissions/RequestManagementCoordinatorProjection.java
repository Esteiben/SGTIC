package com.uteq.sgtic.projections.admissions;
import java.time.LocalDate;

public interface RequestManagementCoordinatorProjection {
    Integer getId_solicitud();
    String getIdentificacion();
    String getNombres();
    String getApellidos();
    String getCorreo();
    String getCarrera();
    LocalDate getFecha_envio();
    String getEstado();
}
