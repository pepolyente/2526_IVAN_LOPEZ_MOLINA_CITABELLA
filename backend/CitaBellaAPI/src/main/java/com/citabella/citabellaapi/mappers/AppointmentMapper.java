package com.citabella.citabellaapi.mappers;

import com.citabella.citabellaapi.dto.appointment.AppointmentResponse;
import com.citabella.citabellaapi.entity.appointment.Appointment;

import java.util.List;

public class AppointmentMapper {

    public static AppointmentResponse toResponse(Appointment appointment) {
        if (appointment == null) return null;

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getStartAt(),
                appointment.getEndAt(),
                appointment.getStatus(),
                appointment.getNotes(),
                appointment.getHasOverlap(),
                ClientMapper.toResponse(appointment.getClient()),
                EmployeeMapper.toResponse(appointment.getEmployee()),
                TreatmentMapper.toResponseSet(appointment.getTreatments())
        );
    }

    public static List<AppointmentResponse> toResponseList(List<Appointment> appointments) {
        if (appointments == null) return List.of();

        return appointments.stream()
                .map(AppointmentMapper::toResponse)
                .toList();
    }
}
