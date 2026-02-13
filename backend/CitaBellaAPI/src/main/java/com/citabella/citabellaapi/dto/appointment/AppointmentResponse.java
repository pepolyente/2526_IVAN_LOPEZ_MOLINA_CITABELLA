package com.citabella.citabellaapi.dto.appointment;

import com.citabella.citabellaapi.dto.client.ClientResponse;
import com.citabella.citabellaapi.dto.employee.EmployeeResponse;
import com.citabella.citabellaapi.dto.treatment.TreatmentResponse;
import com.citabella.citabellaapi.entity.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.Set;

public record AppointmentResponse(
        Integer id,
        LocalDateTime startAt,
        LocalDateTime endAt,
        AppointmentStatus status,
        String notes,
        boolean hasOverlap,
        ClientResponse client,
        EmployeeResponse employee,
        Set<TreatmentResponse> treatments
) {
}
