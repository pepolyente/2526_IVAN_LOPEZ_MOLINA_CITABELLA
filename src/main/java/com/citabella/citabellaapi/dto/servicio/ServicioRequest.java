package com.citabella.citabellaapi.dto.servicio;

import java.math.BigDecimal;

public record ServicioRequest(
        String nombre,
        String descripcion,
        Integer duracionMin,
        Integer duracionMax,
        BigDecimal precio
) {
}
