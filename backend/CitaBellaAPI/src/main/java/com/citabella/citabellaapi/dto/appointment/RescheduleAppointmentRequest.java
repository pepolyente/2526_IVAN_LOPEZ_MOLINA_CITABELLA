package com.citabella.citabellaapi.dto.appointment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

public record RescheduleAppointmentRequest(
        @NotNull
        Integer id,
        @NotNull
        Integer employeeId,
        @NotNull
        @NotEmpty
        Set<Integer> treatmentsIds,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String notes
) {
}
