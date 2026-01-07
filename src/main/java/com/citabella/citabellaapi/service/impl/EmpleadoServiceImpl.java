package com.citabella.citabellaapi.service.impl;

import com.citabella.citabellaapi.dto.empleado.EmpleadoRequest;
import com.citabella.citabellaapi.dto.empleado.EmpleadoResponse;
import com.citabella.citabellaapi.entity.empleado.Empleado;
import com.citabella.citabellaapi.repository.EmpleadoRepository;
import com.citabella.citabellaapi.service.interfaces.EmpleadoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    @Override
    public EmpleadoResponse crear(EmpleadoRequest request) {

        if (request.nombre() == null || request.nombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (empleadoRepository.existsByNombre(request.nombre())){
            throw new IllegalArgumentException("Ya existe un empleado con ese nombre");
        }

        Empleado empleado = new Empleado();
        empleado.setNombre(request.nombre());


        Empleado guardado = empleadoRepository.save(empleado);
        return mapToResponse(guardado);
    }

    @Override
    public EmpleadoResponse obtenerPorId(Integer id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        return mapToResponse(empleado);
    }

    @Override
    public List<EmpleadoResponse> listar() {
        return empleadoRepository.findAll().stream()
                .map(this::mapToResponse).toList();
    }

    @Override
    public EmpleadoResponse desactivar(Integer id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Empleado no encontrado"));
        empleado.setActivo(false);
        Empleado actualizado = empleadoRepository.save(empleado);

        return mapToResponse(actualizado);
    }

    private EmpleadoResponse mapToResponse(Empleado empleado) {
        return new EmpleadoResponse(
                empleado.getIdEmpleado(),
                empleado.getNombre(),
                empleado.getPuesto(),
                empleado.getActivo()
        );
    }

    /*
    @Override
    public List<Empleado> listarActivos() {
        return empleadoRepository.findByActivoTrue();
    }
    */
}
