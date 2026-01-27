package com.citabella.citabellaapi.dto.client;

import com.citabella.citabellaapi.entity.enums.Gender;

public record ClienteResponse(
        Integer idCliente,
        String nombre,
        String telefono,
        Gender gender
) {}
