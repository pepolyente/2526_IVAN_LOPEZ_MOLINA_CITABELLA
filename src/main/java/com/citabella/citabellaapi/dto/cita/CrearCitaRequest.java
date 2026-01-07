package com.citabella.citabellaapi.dto.cita;

import java.time.LocalDateTime;

public record CrearCitaRequest(
        Integer idCliente,
        Integer idEmpleado,
        Integer idServicio,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        String notas
) {
}
