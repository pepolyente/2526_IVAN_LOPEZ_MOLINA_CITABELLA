package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.empleado.EmpleadoRequest;
import com.citabella.citabellaapi.dto.empleado.EmpleadoResponse;
import com.citabella.citabellaapi.entity.empleado.Empleado;

import java.util.List;

public interface EmpleadoService {
    EmpleadoResponse crear(EmpleadoRequest request);
    EmpleadoResponse obtenerPorId(Integer id);
    List<EmpleadoResponse> listar();
    EmpleadoResponse desactivar(Integer idEmpleado);
    /*List<Empleado> listarActivos();*/
}
