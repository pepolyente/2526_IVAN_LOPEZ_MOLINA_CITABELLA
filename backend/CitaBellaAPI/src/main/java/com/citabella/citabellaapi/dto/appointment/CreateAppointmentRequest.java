package com.citabella.citabellaapi.dto.appointment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

public record CreateAppointmentRequest(
        @NotNull
        Integer clientId,
        @NotNull
        Integer employeeId,
        @NotNull
        @NotEmpty
        Set<Integer> treatmentsIds,
        @NotNull
        LocalDateTime startAt,
        @NotNull
        LocalDateTime endAt,
        String notes
) {
}
