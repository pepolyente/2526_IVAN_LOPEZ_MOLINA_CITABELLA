package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.entity.empleado.EmpleadoServicio;

import java.util.List;

public interface EmpleadoServicioService {
    void asignar(Integer idEmpleado, Integer idServicio);
    /*List<EmpleadoServicio> listarPorEmpleado(Integer idEmpleado);*/
}
