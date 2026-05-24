package com.citabella.citabellaapi.dto.treatment;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TreatmentResponse(
        Integer id,
        @NotNull
        String name,
        @NotNull
        Integer minimumDuration,
        @NotNull
        BigDecimal price,
        Boolean active
) {
}
