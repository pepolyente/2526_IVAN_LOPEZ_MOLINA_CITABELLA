package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.appointment.CitaResponse;
import com.citabella.citabellaapi.dto.appointment.CrearCitaRequest;
import com.citabella.citabellaapi.entity.appointment.Appointment;
import com.citabella.citabellaapi.entity.enums.AppointmentStatus;
import com.citabella.citabellaapi.entity.sale.Sale;

import java.util.List;

public interface AppointmentService {
    CitaResponse create(CrearCitaRequest request);

    CitaResponse cancel(CrearCitaRequest request);

    CitaResponse closeAppointment(Integer clientId);

    List<Appointment> getAllByEmployeeId(Integer employeeId);

    List<Appointment> getAllByClientId(Integer clientId);

    void validateStatusChange(AppointmentStatus currentStatus, AppointmentStatus nextStatus);

    boolean hasOverlap();

    Sale checkout();

}
