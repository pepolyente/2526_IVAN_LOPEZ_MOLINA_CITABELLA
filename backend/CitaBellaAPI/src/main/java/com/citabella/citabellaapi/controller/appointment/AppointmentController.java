package com.citabella.citabellaapi.controller.appointment;

import com.citabella.citabellaapi.dto.appointment.AppointmentResponse;
import com.citabella.citabellaapi.dto.appointment.CreateAppointmentRequest;
import com.citabella.citabellaapi.service.interfaces.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Appointments", description = "Appointment management")
@RestController
@RequestMapping("api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(
            summary = "Create appointment",
            description = "Requires authentication. Allowed roles: ADMIN, EMPLOYEE")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@RequestBody CreateAppointmentRequest request) {
        AppointmentResponse response = appointmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
