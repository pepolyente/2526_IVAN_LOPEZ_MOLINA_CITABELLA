package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.appointment.*;
import com.citabella.citabellaapi.dto.client.ClientResponse;
import com.citabella.citabellaapi.dto.employee.EmployeeResponse;
import com.citabella.citabellaapi.dto.treatment.TreatmentResponse;
import com.citabella.citabellaapi.entity.appointment.Appointment;
import com.citabella.citabellaapi.entity.client.Client;
import com.citabella.citabellaapi.entity.employee.Employee;
import com.citabella.citabellaapi.entity.treatment.Treatment;
import com.citabella.citabellaapi.entity.enums.AppointmentStatus;
import com.citabella.citabellaapi.entity.sale.Sale;
import com.citabella.citabellaapi.exception.*;
import com.citabella.citabellaapi.mappers.AppointmentMapper;
import com.citabella.citabellaapi.repository.*;
import com.citabella.citabellaapi.service.interfaces.AppointmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final TreatmentRepository treatmentRepository;
    private final EmployeeTreatmentRepository employeeTreatmentRepository;

    @Override
    public Page<AppointmentResponse> findAll(Pageable pageable, AppointmentStatus status) {
        if (status != null) {
            return appointmentRepository.findAllByStatus(status, pageable)
                    .map(AppointmentMapper::toResponse);
        }
        return appointmentRepository.findAll(pageable)
                .map(AppointmentMapper::toResponse);
    }

    @Override
    public AppointmentResponse getById(Integer id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        return AppointmentMapper.toResponse(appointment);
    }

    @Override
    public AppointmentResponse create(CreateAppointmentRequest request) {

        if (request.startAt() == null || request.endAt() == null) {
            throw new BadRequestException("Start and end dates are required");
        }
        if (!request.endAt().isAfter(request.startAt())) {
            throw new BadRequestException("End date must be after start date");
        }

        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        if (!employee.getActive()) {
            throw new BadRequestException("Employee not active");
        }

        List<Treatment> treatments = treatmentRepository.findAllById(request.treatmentsIds());

        boolean hasOverlap = appointmentRepository.hasOverlap(
                employee.getId(),
                request.startAt(),
                request.endAt()
        );

        Appointment appointment = new Appointment();
        appointment.setHasOverlap(hasOverlap);
        appointment.setClient(client);
        appointment.setEmployee(employee);
        appointment.setTreatments(treatments);
        appointment.setStartAt(request.startAt());
        appointment.setEndAt(request.endAt());
        if (request.notes() != null && !request.notes().isBlank()) {
            appointment.setNotes(request.notes());
        }

        Appointment saved = appointmentRepository.save(appointment);

        List<TreatmentResponse> treatmentResponses = new ArrayList<>();
        for (Treatment t : treatments) {
            treatmentResponses.add(new TreatmentResponse(
                    t.getId(), t.getName(), t.getMinimumDuration(), t.getPrice(), t.getActive()));
        }

        return new AppointmentResponse(
                saved.getId(), saved.getStartAt(), saved.getEndAt(),
                saved.getStatus(), saved.getNotes(), hasOverlap,
                new ClientResponse(client.getId(), client.getName(), client.getPhoneNumber(), client.getGender()),
                new EmployeeResponse(employee.getId(), employee.getName(), employee.getPosition(), employee.getActive()),
                treatmentResponses
        );
    }

    @Override
    public AppointmentResponse update(RescheduleAppointmentRequest request) {
        if (request.startAt() == null || request.endAt() == null) {
            throw new BadRequestException("Start and end dates are required");
        }
        if (!request.endAt().isAfter(request.startAt())) {
            throw new BadRequestException("End date must be after start date");
        }

        Appointment appointment = appointmentRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED
                || appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot update a " + appointment.getStatus() + " appointment");
        }

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        List<Treatment> treatments = treatmentRepository.findAllById(request.treatmentsIds());

        appointment.setEmployee(employee);
        appointment.setTreatments(treatments);
        appointment.setStartAt(request.startAt());
        appointment.setEndAt(request.endAt());
        appointment.setNotes(request.notes());

        return AppointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse changeStatus(Integer id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        validateStatusChange(appointment.getStatus(), status);
        appointment.setStatus(status);
        return AppointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse cancel(Integer id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Appointment is already cancelled");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return AppointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public List<Appointment> getAllByEmployeeId(Integer employeeId) {
        return appointmentRepository.findByEmployee_Id(employeeId);
    }

    @Override
    public List<Appointment> getAllByClientId(Integer clientId) {
        return appointmentRepository.findByClient_Id(clientId);
    }

    @Override
    public void validateStatusChange(AppointmentStatus currentStatus, AppointmentStatus nextStatus) {
        boolean valid = switch (currentStatus) {
            case PENDING -> nextStatus == AppointmentStatus.CONFIRMED || nextStatus == AppointmentStatus.CANCELLED;
            case CONFIRMED ->
                    nextStatus == AppointmentStatus.IN_PROGRESS || nextStatus == AppointmentStatus.CANCELLED || nextStatus == AppointmentStatus.NO_SHOW;
            case IN_PROGRESS -> nextStatus == AppointmentStatus.COMPLETED || nextStatus == AppointmentStatus.CANCELLED;
            default -> false;
        };
        if (!valid) {
            throw new BadRequestException("Invalid status transition: " + currentStatus + " → " + nextStatus);
        }
    }

    @Override
    public boolean hasOverlap() {
        return false;
    }

    @Override
    public Sale checkout() {
        return null;
    }
}
