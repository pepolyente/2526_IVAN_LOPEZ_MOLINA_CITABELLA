package com.citabella.citabellaapi.dto.client;

import com.citabella.citabellaapi.entity.enums.Genero;

import java.time.LocalDate;

public record ClienteRequest(
        String nombre,
        String telefono,
        Genero genero,//String
        LocalDate fechaNacimiento
) {}
