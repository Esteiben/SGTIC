package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "auditoria_sistema")
public class AuditoriaSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Integer idAuditoria;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "modulo_tabla")
    private String moduloTabla;

    @Column(name = "accion")
    private String accion;

    @Column(name = "valores_anteriores", columnDefinition = "jsonb")
    private String valoresAnteriores;

    @Column(name = "valores_nuevos", columnDefinition = "jsonb")
    private String valoresNuevos;

    @Column(name = "direccion_ip")
    private String direccionIp;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;
}