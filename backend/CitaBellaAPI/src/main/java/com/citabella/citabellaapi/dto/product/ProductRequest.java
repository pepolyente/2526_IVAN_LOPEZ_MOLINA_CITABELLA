package com.citabella.citabellaapi.dto.product;

import com.citabella.citabellaapi.entity.enums.UsageType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Name is required")
        String name,

        String category,

        @DecimalMin(value = "0.0", message = "Purchase price must be >= 0")
        BigDecimal purchasePrice,

        @DecimalMin(value = "0.0", message = "Sale price must be >= 0")
        BigDecimal salePrice,

        UsageType usageType,

        String supplier,

        Boolean isCritical,

        String imageKey
) {
}
