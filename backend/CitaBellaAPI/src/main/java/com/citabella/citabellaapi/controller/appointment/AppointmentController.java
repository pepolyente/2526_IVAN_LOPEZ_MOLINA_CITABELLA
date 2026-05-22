package com.citabella.citabellaapi.controller.appointment;

import com.citabella.citabellaapi.docs.ApiSecurityDocs;
import com.citabella.citabellaapi.dto.appointment.AppointmentResponse;
import com.citabella.citabellaapi.dto.appointment.CreateAppointmentRequest;
import com.citabella.citabellaapi.dto.appointment.RescheduleAppointmentRequest;
import com.citabella.citabellaapi.dto.appointment.UpdateStatusAppointmentRequest;
import com.citabella.citabellaapi.dto.page.PageResponse;
import com.citabella.citabellaapi.entity.enums.AppointmentStatus;
import com.citabella.citabellaapi.service.interfaces.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Appointments", description = "Appointment management")
@RestController
@RequestMapping("api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "Get all appointments (paginated, filterable by status)")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public ResponseEntity<PageResponse<AppointmentResponse>> findAll(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) AppointmentStatus status) {

        return ResponseEntity.ok(PageResponse.from(
                appointmentService.findAll(pageable, status)));
    }

    @Operation(summary = "Get my appointments (for client)", description = "Obtiene las citas del cliente autenticado")
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/my")
    public ResponseEntity<PageResponse<AppointmentResponse>> getMyAppointments(
            @ParameterObject Pageable pageable,
            Authentication authentication) {

        String username = authentication.getName();
        return ResponseEntity.ok(appointmentService.findByAuthenticatedClient(pageable, username));
    }

    @Operation(summary = "Get appointment by ID", description = ApiSecurityDocs.ADMIN_EMPLOYEE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getById(@PathVariable Integer id) {

        return ResponseEntity.ok(appointmentService.getById(id));
    }

    @Operation(summary = "Create appointment", description = ApiSecurityDocs.ADMIN_EMPLOYEE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(
            @Valid @RequestBody CreateAppointmentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.create(request));
    }

    @Operation(summary = "Update appointment (reschedule)", description = ApiSecurityDocs.ADMIN_EMPLOYEE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PutMapping("/update")
    public ResponseEntity<AppointmentResponse> update(
            @Valid @RequestBody RescheduleAppointmentRequest request) {

        return ResponseEntity.ok(appointmentService.update(request));
    }

    @Operation(
            summary = "Change appointment status",
            description = ApiSecurityDocs.ADMIN_EMPLOYEE +
                    "\nTransitions: PENDING→CONFIRMED|CANCELLED, CONFIRMED→IN_PROGRESS|CANCELLED|NO_SHOW, IN_PROGRESS→COMPLETED|CANCELLED")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponse> changeStatus(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateStatusAppointmentRequest request) {

        return ResponseEntity.ok(appointmentService.changeStatus(id, request.status()));
    }

    @Operation(
            summary = "Cancel appointment (logical delete)",
            description = ApiSecurityDocs.ADMIN_EMPLOYEE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<AppointmentResponse> cancel(@PathVariable Integer id) {

        return ResponseEntity.ok(appointmentService.cancel(id));
    }
}
