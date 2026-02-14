package com.citabella.citabellaapi.dto.product;

import java.math.BigDecimal;

public record ProductPublicResponse(
        Integer id,
        String name,
        String category,
        BigDecimal salePrice,
        String imageKey
) {
}
