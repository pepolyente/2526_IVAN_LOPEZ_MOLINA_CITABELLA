package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.employee.EmpleadoRequest;
import com.citabella.citabellaapi.dto.employee.EmpleadoResponse;
import com.citabella.citabellaapi.entity.employee.Employee;
import com.citabella.citabellaapi.repository.EmployeeRepository;
import com.citabella.citabellaapi.service.interfaces.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmpleadoResponse crear(EmpleadoRequest request) {

        if (request.nombre() == null || request.nombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (employeeRepository.existsByName(request.nombre())){
            throw new IllegalArgumentException("Ya existe un empleado con ese nombre");
        }

        Employee employee = new Employee();
        employee.setName(request.nombre());


        Employee guardado = employeeRepository.save(employee);
        return mapToResponse(guardado);
    }

    @Override
    public EmpleadoResponse obtenerPorId(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        return mapToResponse(employee);
    }

    @Override
    public List<EmpleadoResponse> listar() {
        return employeeRepository.findAll().stream()
                .map(this::mapToResponse).toList();
    }

    @Override
    public EmpleadoResponse desactivar(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Empleado no encontrado"));
        employee.setActive(false);
        Employee actualizado = employeeRepository.save(employee);

        return mapToResponse(actualizado);
    }

    private EmpleadoResponse mapToResponse(Employee employee) {
        return new EmpleadoResponse(
                employee.getId(),
                employee.getName(),
                employee.getPosition(),
                employee.getActive()
        );
    }

    /*
    @Override
    public List<Empleado> listarActivos() {
        return empleadoRepository.findByActivoTrue();
    }
    */
}
