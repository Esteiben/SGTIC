package com.uteq.sgtic.dtos.banckTemaDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BanckTemaDTO {
    private Integer idTema;
    private String titulo;
    private String descripcion;
    private String nombreOpcion;
    private Integer idOpcion;
    private String nombreComision;
}
