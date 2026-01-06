package com.citabella.citabellaapi.dto.empleado;

public record EmpleadoResponse(
        Integer idEmpleado,
        String nombre,
        String puesto,
        Boolean activo
) {}
