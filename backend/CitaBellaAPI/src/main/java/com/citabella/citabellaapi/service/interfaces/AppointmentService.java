package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.appointment.AppointmentResponse;
import com.citabella.citabellaapi.dto.appointment.CreateAppointmentRequest;
import com.citabella.citabellaapi.dto.appointment.RescheduleAppointmentRequest;
import com.citabella.citabellaapi.dto.page.PageResponse;
import com.citabella.citabellaapi.entity.appointment.Appointment;
import com.citabella.citabellaapi.entity.enums.AppointmentStatus;
import com.citabella.citabellaapi.entity.sale.Sale;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AppointmentService {
    Page<AppointmentResponse> findAll(Pageable pageable, AppointmentStatus status);

    AppointmentResponse getById(Integer id);


    AppointmentResponse create(CreateAppointmentRequest request);

    AppointmentResponse update(@Valid RescheduleAppointmentRequest request);

    AppointmentResponse changeStatus(Integer id, AppointmentStatus status);

    AppointmentResponse cancel(Integer id);

    List<Appointment> getAllByEmployeeId(Integer employeeId);

    PageResponse<AppointmentResponse> findByAuthenticatedClient(Pageable pageable, String username);
    void validateStatusChange(AppointmentStatus currentStatus, AppointmentStatus nextStatus);
    boolean hasOverlap();
    Sale checkout();
}
