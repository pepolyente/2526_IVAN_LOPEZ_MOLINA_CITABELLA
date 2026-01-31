package com.citabella.citabellaapi.entity.product;

import com.citabella.citabellaapi.entity.enums.UsageType;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,length = 100)
    private String name;

    @Column(length = 100)
    private String category;

    private BigDecimal purchasePrice;

    private BigDecimal salePrice;

    @Enumerated(value = EnumType.STRING)
    private UsageType usageType;

    @Column(length = 100)
    private String supplier;

    private Boolean isCritical;

    private Boolean active;

    private String imageKey;

    @PrePersist
    private void prePersist() {
        if (usageType == null) {
            usageType = UsageType.BOTH;
        }
        if (isCritical == null) {
            isCritical = false;
        }
        if (active == null) {
            active = true;
        }
    }
}
