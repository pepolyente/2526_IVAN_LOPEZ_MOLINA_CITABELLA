package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.appointment.CitaResponse;
import com.citabella.citabellaapi.dto.appointment.CrearCitaRequest;
import com.citabella.citabellaapi.entity.appointment.Appointment;
import com.citabella.citabellaapi.entity.enums.AppointmentStatus;
import com.citabella.citabellaapi.entity.sale.Sale;

import java.util.List;

public interface CitaService {
    CitaResponse crear(CrearCitaRequest request);

    CitaResponse cancelarCita(CrearCitaRequest request);

    CitaResponse finalizarCita(Integer idCita);

    List<Appointment> listarPorEmpleado(Integer idEmpleado);

    List<Appointment> listarPorCliente(Integer idCliente);

    void validarCambioDeEstado(AppointmentStatus appointmentStatusAnterior, AppointmentStatus appointmentStatusNuevo);

    boolean detectarSolape();

    Sale cerrarCita();

}
