package com.citabella.citabellaapi.entity.producto;

import com.citabella.citabellaapi.entity.utiles.ReferenciaTipo;
import com.citabella.citabellaapi.entity.utiles.TipoMovimiento;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_stock")
public class MovimientoStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMovimiento;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private Integer cantidad;

    private String motivo;

    private LocalDateTime fecha;

    @Enumerated(value = EnumType.STRING)
    private ReferenciaTipo referenciaTipo;

    private Integer referenciaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_stock", nullable = false)
    private Stock stock;

    @PrePersist
    private void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }

}
