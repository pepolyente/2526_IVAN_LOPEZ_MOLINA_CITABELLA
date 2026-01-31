package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.appointment.CitaResponse;
import com.citabella.citabellaapi.dto.appointment.CrearCitaRequest;
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
    public CitaResponse create(CrearCitaRequest request) {

        if (request.fechaInicio() == null || request.fechaFin() == null) {
            throw new IllegalArgumentException("Las fechas son obligatorias");
        }

        if (!request.fechaFin().isAfter(request.fechaInicio())) {
            throw new IllegalArgumentException("La fecha fin debe ser posterior a la fecha inicio");
        }

        Client client = clientRepository.findById(request.idCliente())
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe"));

        Employee employee = employeeRepository.findById(request.idEmpleado())
                .orElseThrow(() -> new IllegalArgumentException("El empleado no existe"));

        Treatment treatment = treatmentRepository.findById(request.idServicio())
                .orElseThrow(() -> new IllegalArgumentException("El servicio no existe"));

        boolean haySolape = appointmentRepository.has_overlap(
                employee.getId(),
                request.fechaInicio(),
                request.fechaFin()
        );

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setEmployee(employee);

        //CAMBIAR A SET
        //cita.setServicio(servicio);


        appointment.setStartAt(request.fechaInicio());
        appointment.setEndAt(request.fechaFin());
        appointment.setNotes(request.notas());

        appointmentRepository.save(appointment);

        return new CitaResponse(
                appointment.getId(),
                haySolape
        );
    }

    @Override
    public CitaResponse cancel(CrearCitaRequest request) {
        return null;
    }

    @Override
    public CitaResponse closeAppointment(Integer clientId) {
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
