package com.citabella.citabellaapi.entity.product;

import jakarta.persistence.*;

@Entity
@Table(name = "almacen")
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAlmacen;

    @Column(nullable = false,length = 100)
    private String nombre;

    @Column(length = 150)
    private String ubicacion;

    private Integer capacidad;
}
