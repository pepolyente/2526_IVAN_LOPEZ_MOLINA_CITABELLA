package com.citabella.citabellaapi.dto.treatment;

import java.math.BigDecimal;

public record TreatmentRequest(
        String name,
        String description,
        Integer minimumDuration,
        Integer maximumDuration,
        BigDecimal price
) {
}
