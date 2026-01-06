package com.citabella.citabellaapi.dto.cliente;

import com.citabella.citabellaapi.entity.utiles.Genero;

public record ClienteResponse(
        Integer idCliente,
        String nombre,
        String telefono,
        Genero genero
) {}
