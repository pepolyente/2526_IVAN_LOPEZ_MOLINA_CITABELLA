package com.citabella.citabellaapi.dto.appointment;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        Integer clientId,
        Integer employeeId,
        Integer treatmentId,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String notes
) {
}
