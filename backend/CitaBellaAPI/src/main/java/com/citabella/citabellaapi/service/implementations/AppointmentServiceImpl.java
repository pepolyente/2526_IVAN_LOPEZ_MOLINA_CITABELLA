package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.appointment.AppointmentResponse;
import com.citabella.citabellaapi.dto.appointment.CreateAppointmentRequest;
import com.citabella.citabellaapi.entity.appointment.Appointment;
import com.citabella.citabellaapi.entity.client.Client;
import com.citabella.citabellaapi.entity.employee.Employee;
import com.citabella.citabellaapi.entity.treatment.Treatment;
import com.citabella.citabellaapi.entity.enums.AppointmentStatus;
import com.citabella.citabellaapi.entity.sale.Sale;
import com.citabella.citabellaapi.repository.*;
import com.citabella.citabellaapi.service.interfaces.AppointmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public AppointmentResponse create(CreateAppointmentRequest request) {

        if (request.startAt() == null || request.endAt() == null) {
            throw new IllegalArgumentException("Start and end dates are required");
        }

        if (!request.endAt().isAfter(request.startAt())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        Treatment treatment = treatmentRepository.findById(request.treatmentId())
                .orElseThrow(() -> new IllegalArgumentException("Treatment not found"));

        boolean hasOverlap = appointmentRepository.has_overlap(
                employee.getId(),
                request.startAt(),
                request.endAt()
        );

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setEmployee(employee);

        // TODO: change to Set<Treatment>


        appointment.setStartAt(request.startAt());
        appointment.setEndAt(request.endAt());
        appointment.setNotes(request.notes());

        appointmentRepository.save(appointment);

        return new AppointmentResponse(
                appointment.getId(),
                hasOverlap
        );
    }

    @Override
    public AppointmentResponse cancel(CreateAppointmentRequest request) {
        return null;
    }

    @Override
    public AppointmentResponse closeAppointment(Integer clientId) {
        return null;
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
