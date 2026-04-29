package com.citabella.citabellaapi.dto.appointment;

import com.citabella.citabellaapi.entity.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusAppointmentRequest(
        @NotNull AppointmentStatus status
) {
}
