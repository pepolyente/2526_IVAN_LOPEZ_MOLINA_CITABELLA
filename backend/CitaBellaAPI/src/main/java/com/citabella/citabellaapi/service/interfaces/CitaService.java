package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.appointment.CitaResponse;
import com.citabella.citabellaapi.dto.appointment.CrearCitaRequest;
import com.citabella.citabellaapi.entity.appointment.Cita;
import com.citabella.citabellaapi.entity.enums.EstadoCita;
import com.citabella.citabellaapi.entity.sale.Venta;

import java.util.List;

public interface CitaService {
    CitaResponse crear(CrearCitaRequest request);

    CitaResponse cancelarCita(CrearCitaRequest request);

    CitaResponse finalizarCita(Integer idCita);

    List<Cita> listarPorEmpleado(Integer idEmpleado);
    List<Cita> listarPorCliente(Integer idCliente);

    void validarCambioDeEstado(EstadoCita estadoCitaAnterior, EstadoCita estadoCitaNuevo);

    boolean detectarSolape();

    Venta cerrarCita();

}
