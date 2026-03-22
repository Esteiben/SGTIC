package com.uteq.sgtic.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@Entity
@Table(name = "tema", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BanckTema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tema")
    private Integer idTema;

    @Column(name = "id_carrera", nullable = false)
    private Integer idCarrera;

    @Column(name = "id_comision", nullable = false)
    private Integer idComision;

    @Column(name = "titulo", nullable = false, length = 300)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "es_banco", nullable = false)
    private Boolean esBanco;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Column(name = "id_opcion", nullable = false)
    private Integer idOpcion;
}
