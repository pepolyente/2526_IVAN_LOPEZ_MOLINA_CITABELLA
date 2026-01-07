package com.citabella.citabellaapi.entity.venta;

import com.citabella.citabellaapi.entity.producto.Producto;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_venta_producto")
public class DetalleVentaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalle;

    private Integer cantidad;

    @Column(nullable = false)
    private BigDecimal precioUnitario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta",nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto",nullable = false)
    private Producto producto;

    @PrePersist
    private void prePersist() {
        if (cantidad == null) {
            cantidad = 1;
        }
    }

}
