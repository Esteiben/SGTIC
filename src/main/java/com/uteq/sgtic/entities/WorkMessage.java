package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mensaje_trabajo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class WorkMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_mensaje")
    private Integer idMessage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_trabajo", nullable = false)
    private DegreeWork degreeWork;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario_emisor", nullable = false)
    private User sender;

    @Column(name = "tipo", nullable = false, length = 30)
    private String type;

    @Column(name = "contenido", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime sentDate;

    @Column(name = "visible_estudiante", nullable = false)
    private Boolean visibleToStudent;
}
