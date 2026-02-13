package com.citabella.citabellaapi.dto.treatment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TreatmentRequest(
        String name,
        String description,
        Integer minimumDuration,
        Integer maximumDuration,
        @DecimalMin(value = "0.0")
        BigDecimal price
) {
}
