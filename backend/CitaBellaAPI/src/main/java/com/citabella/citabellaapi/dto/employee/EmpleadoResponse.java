package com.citabella.citabellaapi.dto.employee;

public record EmpleadoResponse(
        Integer idEmpleado,
        String nombre,
        String puesto,
        Boolean activo
) {}
