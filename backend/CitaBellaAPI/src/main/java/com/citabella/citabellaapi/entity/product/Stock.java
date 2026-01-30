package com.citabella.citabellaapi.entity.product;

import jakarta.persistence.*;

@Entity
@Table(
        name = "stock",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_warehouse", "id_product"})
)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer currentStock;

    private Integer minimumStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_warehouse")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product")
    private Product product;

    @PrePersist
    private void prePersist() {
        if (currentStock == null) {
            currentStock = 0;
        }
        if (minimumStock == null) {
            minimumStock = 0;
        }
    }

}
