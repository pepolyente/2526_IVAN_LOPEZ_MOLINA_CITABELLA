package com.citabella.citabellaapi.dto.client;

import com.citabella.citabellaapi.entity.enums.Gender;

import java.time.LocalDate;

public record ClienteRequest(
        String nombre,
        String telefono,
        Gender gender,//String
        LocalDate fechaNacimiento
) {}
