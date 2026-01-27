package com.citabella.citabellaapi.entity.product;

import com.citabella.citabellaapi.entity.enums.ReferenceType;
import com.citabella.citabellaapi.entity.enums.MovementType;
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
    private MovementType tipo;

    @Column(nullable = false)
    private Integer cantidad;

    private String motivo;

    private LocalDateTime fecha;

    @Enumerated(value = EnumType.STRING)
    private ReferenceType referenceType;

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
