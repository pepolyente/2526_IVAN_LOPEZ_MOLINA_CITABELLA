package com.citabella.citabellaapi.dto.cliente;

import com.citabella.citabellaapi.entity.utiles.Genero;

import java.time.LocalDate;

public record ClienteRequest(
        String nombre,
        String telefono,
        Genero genero,//String
        LocalDate fechaNacimiento
) {}
