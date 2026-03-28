package com.uteq.sgtic.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "credencial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_credencial")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", unique = true, nullable = false)
    private User user;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash",columnDefinition = "TEXT", nullable = false)
    private String passwordHash;
}
