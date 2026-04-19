package com.citabella.citabellaapi.controller.appointment;

import com.citabella.citabellaapi.docs.ApiSecurityDocs;
import com.citabella.citabellaapi.dto.appointment.AppointmentResponse;
import com.citabella.citabellaapi.dto.appointment.CreateAppointmentRequest;
import com.citabella.citabellaapi.dto.appointment.RescheduleAppointmentRequest;
import com.citabella.citabellaapi.dto.page.PageResponse;
import com.citabella.citabellaapi.service.interfaces.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Appointments", description = "Appointment management")
@RestController
@RequestMapping("api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(
            summary = "Get all appointments")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public ResponseEntity<PageResponse<AppointmentResponse>> findAll(
            @ParameterObject Pageable pageable) {

        Page<AppointmentResponse> page = appointmentService.findAll(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @Operation(
            summary = "Create appointment",
            description = "Requires authentication. Allowed roles: ADMIN, EMPLOYEE")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody CreateAppointmentRequest request) {
        AppointmentResponse response = appointmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Update appointment",
            description = "Requires authentication. Allowed roles: ADMIN, EMPLOYEE")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PutMapping("/update")
    public ResponseEntity<AppointmentResponse> update(@Valid @RequestBody RescheduleAppointmentRequest request) {
        AppointmentResponse response = appointmentService.update(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
