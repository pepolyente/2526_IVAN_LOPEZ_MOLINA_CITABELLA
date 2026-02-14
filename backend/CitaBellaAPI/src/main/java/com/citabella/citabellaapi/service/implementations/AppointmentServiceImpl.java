package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.appointment.AppointmentResponse;
import com.citabella.citabellaapi.dto.appointment.CreateAppointmentRequest;
import com.citabella.citabellaapi.dto.client.ClientResponse;
import com.citabella.citabellaapi.dto.employee.EmployeeResponse;
import com.citabella.citabellaapi.dto.treatment.TreatmentResponse;
import com.citabella.citabellaapi.entity.appointment.Appointment;
import com.citabella.citabellaapi.entity.client.Client;
import com.citabella.citabellaapi.entity.employee.Employee;
import com.citabella.citabellaapi.entity.treatment.Treatment;
import com.citabella.citabellaapi.entity.enums.AppointmentStatus;
import com.citabella.citabellaapi.entity.sale.Sale;
import com.citabella.citabellaapi.exception.BadRequestException;
import com.citabella.citabellaapi.repository.*;
import com.citabella.citabellaapi.service.interfaces.AppointmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
//        if (!client.isActive()){
//            throw new BadRequestException("Client not active");
//        }

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        if (!employee.getActive()) {
            throw new BadRequestException("Employee not active");
        }

        Set<Treatment> treatments = new HashSet<>();
        for (int i : request.treatmentsIds()) {
            Treatment treatment = treatmentRepository.findById(i)
                    .orElseThrow(() -> new IllegalArgumentException("Treatment not found"));
            treatments.add(treatment);
        }


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
        if (!request.notes().isBlank()) {
            appointment.setNotes(request.notes());
        }

        appointment.setStartAt(request.startAt());
        appointment.setEndAt(request.endAt());
        appointment.setNotes(request.notes());

        appointmentRepository.save(appointment);
        Set<TreatmentResponse> treatmentResponses = new HashSet<>();
        for (Treatment treatment : treatments) {
            treatmentResponses.add(new TreatmentResponse(
                    treatment.getId(),
                    treatment.getName(),
                    treatment.getMinimumDuration(),
                    treatment.getPrice(),
                    treatment.getActive()
            ));
        }

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getStartAt(),
                appointment.getEndAt(),
                appointment.getStatus(),
                appointment.getNotes(),
                hasOverlap,
                new ClientResponse(
                        client.getId(),
                        client.getName(),
                        client.getPhoneNumber(),
                        client.getGender()
                ),
                new EmployeeResponse(
                        employee.getId(),
                        employee.getName(),
                        employee.getPosition(),
                        employee.getActive()
                ),
                treatmentResponses


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
