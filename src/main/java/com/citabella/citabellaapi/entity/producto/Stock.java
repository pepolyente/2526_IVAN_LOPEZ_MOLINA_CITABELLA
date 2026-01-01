package com.citabella.citabellaapi.entity.producto;

import jakarta.persistence.*;

@Entity
@Table(
        name = "stock",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_almacen", "id_producto"})
)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idStock;

    private Integer stockActual;

    private Integer stockMinimo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_almacen")
    private Almacen almacen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @PrePersist
    private void prePersist() {
        if (stockActual == null) {
            stockActual = 0;
        }
        if (stockMinimo == null) {
            stockMinimo = 0;
        }
    }

}
