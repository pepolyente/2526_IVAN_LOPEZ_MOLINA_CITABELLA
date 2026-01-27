package com.citabella.citabellaapi.entity.client;

import com.citabella.citabellaapi.entity.enums.DeviceType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
//DE MOMENTO SIN USO
@Entity
@Table(name = "token_dispositivo")
public class TokenDispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idToken;

    @Column(nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    private LocalDateTime fechaRegistro;

    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente",nullable = false)
    private Cliente cliente;

    @PrePersist
    private void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
        if (activo == null) {
            activo = true;
        }
    }

}
