package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.cita.CitaResponse;
import com.citabella.citabellaapi.dto.cita.CrearCitaRequest;
import com.citabella.citabellaapi.entity.cita.Cita;
import com.citabella.citabellaapi.entity.utiles.EstadoCita;
import com.citabella.citabellaapi.entity.venta.Venta;

import java.time.LocalDateTime;
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
