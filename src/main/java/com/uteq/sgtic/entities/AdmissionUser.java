package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "solicitud_usuario")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdmissionUser {

    @Id
    @Column(name = "id_solicitud")
    private Integer idRequest;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_solicitud")
    private AdmissionRequest admissionRequest;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", unique = true)
    private User user;
}
