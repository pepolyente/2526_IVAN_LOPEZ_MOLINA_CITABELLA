package com.citabella.citabellaapi.dto.treatment;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TreatmentDetailedResponse(
        Integer id,
        @NotNull
        String name,
        String description,
        @NotNull
        Integer minimumDuration,
        Integer maximumDuration,
        @NotNull
        BigDecimal price,
        Boolean active
) {
}
