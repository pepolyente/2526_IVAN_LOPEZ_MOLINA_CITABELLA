package com.citabella.citabellaapi.service.impl;

import com.citabella.citabellaapi.entity.empleado.Empleado;
import com.citabella.citabellaapi.entity.empleado.EmpleadoServicio;
import com.citabella.citabellaapi.entity.empleado.EmpleadoServicioId;
import com.citabella.citabellaapi.entity.servicio.Servicio;
import com.citabella.citabellaapi.repository.EmpleadoRepository;
import com.citabella.citabellaapi.repository.EmpleadoServicioRepository;
import com.citabella.citabellaapi.repository.ServicioRepository;
import com.citabella.citabellaapi.service.interfaces.EmpleadoServicioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmpleadoServicioServiceImpl implements EmpleadoServicioService {

    private final EmpleadoRepository empleadoRepository;
    private final ServicioRepository servicioRepository;
    private final EmpleadoServicioRepository empleadoServicioRepository;



    @Override
    public void asignar(Integer idEmpleado, Integer idServicio) {

        if (empleadoServicioRepository
                .existsById_IdEmpleadoAndId_IdServicio(idEmpleado, idServicio)) {
            throw new IllegalArgumentException("El servicio ya está asignado al empleado");
        }

        Empleado empleado = empleadoRepository.findById(idEmpleado).orElseThrow();
        Servicio servicio = servicioRepository.findById(idServicio).orElseThrow();

        EmpleadoServicio es = new EmpleadoServicio();
        es.setEmpleado(empleado);
        es.setServicio(servicio);
        es.setId(new EmpleadoServicioId(idEmpleado, idServicio));

        empleadoServicioRepository.save(es);
    }

    /*@Override
    public List<EmpleadoServicio> listarPorEmpleado(Integer idEmpleado) {
        return repo.findByEmpleado_IdEmpleado(idEmpleado);
    }*/
}
