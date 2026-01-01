package com.citabella.citabellaapi.entity.venta;

import com.citabella.citabellaapi.entity.servicio.Servicio;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalleventaservicio")
public class DetalleVentaServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalle;

    private Integer cantidad;

    private BigDecimal precioUnitario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta")
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

    @PrePersist
    private void prePersist() {
        if (cantidad == null) {
            cantidad = 1;
        }
    }

}
