package com.citabella.citabellaapi.dto.treatment;

import java.math.BigDecimal;

public record TreatmentResponse(
        Integer id,
        String name,
        Integer minimumDuration,
        BigDecimal price,
        Boolean active
) {
}
