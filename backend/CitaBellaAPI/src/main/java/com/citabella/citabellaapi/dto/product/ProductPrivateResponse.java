package com.citabella.citabellaapi.dto.product;

import java.math.BigDecimal;

public record ProductPrivateResponse(
        Integer id,
        String name,
        String category,
        BigDecimal purchasePrice,
        BigDecimal salePrice,
        String supplier,
        Boolean isCritical,
        Boolean active,
        String imageKey
) {
}
