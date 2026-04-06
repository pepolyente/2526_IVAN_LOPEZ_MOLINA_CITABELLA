package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.appointment.AppointmentResponse;
import com.citabella.citabellaapi.dto.appointment.CreateAppointmentRequest;
import com.citabella.citabellaapi.entity.appointment.Appointment;
import com.citabella.citabellaapi.entity.enums.AppointmentStatus;
import com.citabella.citabellaapi.entity.sale.Sale;

import java.util.List;

public interface AppointmentService {
    List<AppointmentResponse> findAll();

    AppointmentResponse create(CreateAppointmentRequest request);

    AppointmentResponse cancel(CreateAppointmentRequest request);

    AppointmentResponse closeAppointment(Integer clientId);

    List<Appointment> getAllByEmployeeId(Integer employeeId);

    List<Appointment> getAllByClientId(Integer clientId);

    void validateStatusChange(AppointmentStatus currentStatus, AppointmentStatus nextStatus);

    boolean hasOverlap();

    Sale checkout();

}
