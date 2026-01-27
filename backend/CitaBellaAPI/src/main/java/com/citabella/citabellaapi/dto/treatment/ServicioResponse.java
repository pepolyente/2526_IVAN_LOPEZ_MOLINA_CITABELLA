package com.citabella.citabellaapi.dto.treatment;

import java.math.BigDecimal;

public record ServicioResponse(
        Integer idServicio,
        String nombre,
        Integer duracionMin,
        BigDecimal precio,
        Boolean activo
) {
}
