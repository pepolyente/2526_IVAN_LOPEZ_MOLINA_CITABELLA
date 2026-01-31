package com.citabella.citabellaapi.dto.appointment;

public record AppointmentResponse(
        Integer id,
        boolean hasOverlap
) {
}
