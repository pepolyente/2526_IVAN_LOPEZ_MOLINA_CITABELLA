package com.citabella.citabellaapi.service.impl;

import com.citabella.citabellaapi.dto.cita.CitaResponse;
import com.citabella.citabellaapi.dto.cita.CrearCitaRequest;
import com.citabella.citabellaapi.entity.cita.Cita;
import com.citabella.citabellaapi.entity.cliente.Cliente;
import com.citabella.citabellaapi.entity.empleado.Empleado;
import com.citabella.citabellaapi.entity.servicio.Servicio;
import com.citabella.citabellaapi.entity.utiles.EstadoCita;
import com.citabella.citabellaapi.entity.venta.Venta;
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

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ServicioRepository servicioRepository;
    private final EmpleadoServicioRepository empleadoServicioRepository;


    @Override
    public CitaResponse crear(CrearCitaRequest request) {

        if (request.fechaInicio() == null || request.fechaFin() == null) {
            throw new IllegalArgumentException("Las fechas son obligatorias");
        }

        if (!request.fechaFin().isAfter(request.fechaInicio())) {
            throw new IllegalArgumentException("La fecha fin debe ser posterior a la fecha inicio");
        }

        Cliente cliente = clienteRepository.findById(request.idCliente())
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe"));

        Empleado empleado = empleadoRepository.findById(request.idEmpleado())
                .orElseThrow(() -> new IllegalArgumentException("El empleado no existe"));

        Servicio servicio = servicioRepository.findById(request.idServicio())
                .orElseThrow(() -> new IllegalArgumentException("El servicio no existe"));

        boolean haySolape = citaRepository.existeSolape(
                empleado.getIdEmpleado(),
                request.fechaInicio(),
                request.fechaFin()
        );

        Cita cita = new Cita();
        cita.setCliente(cliente);
        cita.setEmpleado(empleado);

        //CAMBIAR A SET
        //cita.setServicio(servicio);


        cita.setFechaInicio(request.fechaInicio());
        cita.setFechaFin(request.fechaFin());
        cita.setNotas(request.notas());

        citaRepository.save(cita);

        return new CitaResponse(
                cita.getIdCita(),
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
    public List<Cita> listarPorEmpleado(Integer idEmpleado) {
        return citaRepository.findByEmpleado_IdEmpleado(idEmpleado);
    }

    @Override
    public List<Cita> listarPorCliente(Integer idCliente) {
        return citaRepository.findByCliente_IdCliente(idCliente);
    }

    @Override
    public void validarCambioDeEstado(EstadoCita estadoCitaAnterior, EstadoCita estadoCitaNuevo) {

    }

    @Override
    public boolean detectarSolape() {
        return false;
    }

    @Override
    public Venta cerrarCita() {
        return null;
    }
}
