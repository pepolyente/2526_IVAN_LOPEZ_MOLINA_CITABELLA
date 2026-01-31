package com.citabella.citabellaapi.entity.product;

import com.citabella.citabellaapi.entity.enums.ReferenceType;
import com.citabella.citabellaapi.entity.enums.MovementType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movement")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MovementType type;

    @Column(nullable = false)
    private Integer quantity;

    private String reason;

    private LocalDateTime movementDate;

    @Enumerated(value = EnumType.STRING)
    private ReferenceType referenceType;

    private Integer referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_stock", nullable = false)
    private Stock stock;

    @PrePersist
    private void prePersist() {
        if (movementDate == null) {
            movementDate = LocalDateTime.now();
        }
    }

}
