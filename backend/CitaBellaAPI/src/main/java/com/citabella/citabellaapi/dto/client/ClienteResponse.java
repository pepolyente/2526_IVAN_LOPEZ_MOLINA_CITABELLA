package com.citabella.citabellaapi.dto.client;

import com.citabella.citabellaapi.entity.enums.Genero;

public record ClienteResponse(
        Integer idCliente,
        String nombre,
        String telefono,
        Genero genero
) {}
