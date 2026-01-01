package com.citabella.citabellaapi.entity.producto;

import com.citabella.citabellaapi.entity.utiles.TipoUso;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    @Column(nullable = false,length = 100)
    private String nombre;

    @Column(length = 100)
    private String categoria;

    private BigDecimal precioCompra;

    private BigDecimal precioVenta;

    @Enumerated(value = EnumType.STRING)
    private TipoUso tipoUso;

    @Column(length = 100)
    private String proveedor;

    private Boolean prioridadAlerta;

    private Boolean activo;

    @PrePersist
    private void prePersist() {
        if (tipoUso == null) {
            tipoUso = TipoUso.ambos;
        }
        if (prioridadAlerta == null) {
            prioridadAlerta = false;
        }
        if (activo == null) {
            activo = true;
        }
    }
}
