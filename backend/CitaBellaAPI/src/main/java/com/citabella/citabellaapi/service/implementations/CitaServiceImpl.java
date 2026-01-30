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
import com.citabella.citabellaapi.service.interfaces.CitaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final TreatmentRepository treatmentRepository;
    private final EmployeeTreatmentRepository employeeTreatmentRepository;


    @Override
    public CitaResponse crear(CrearCitaRequest request) {

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
    public CitaResponse cancelarCita(CrearCitaRequest request) {
        return null;
    }

    @Override
    public CitaResponse finalizarCita(Integer idCita) {
        return null;
    }

    @Override
    public List<Appointment> listarPorEmpleado(Integer idEmpleado) {
        return appointmentRepository.findByEmployee_Id(idEmpleado);
    }

    @Override
    public List<Appointment> listarPorCliente(Integer idCliente) {
        return appointmentRepository.findByClient_Id(idCliente);
    }

    @Override
    public void validarCambioDeEstado(AppointmentStatus appointmentStatusAnterior, AppointmentStatus appointmentStatusNuevo) {

    }

    @Override
    public boolean detectarSolape() {
        return false;
    }

    @Override
    public Sale cerrarCita() {
        return null;
    }
}
