package com.citabella.citabellaapi.entity.servicio;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idServicio;

    @Column(nullable = false,length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private Integer duracionMin;

    private Integer duracionMax;

    @Column(nullable = false)
    private BigDecimal precio;

    private Boolean activo;

    @PrePersist
    private void prePersist() {
        if (activo == null) {
            activo = true;
        }
    }

}
