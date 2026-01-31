package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.employee.EmpleadoRequest;
import com.citabella.citabellaapi.dto.employee.EmpleadoResponse;
import com.citabella.citabellaapi.entity.employee.Employee;
import com.citabella.citabellaapi.repository.EmployeeRepository;
import com.citabella.citabellaapi.service.interfaces.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmpleadoResponse create(EmpleadoRequest request) {

        if (request.nombre() == null || request.nombre().isBlank()) {
            throw new IllegalArgumentException("Name is mandatory");
        }
        if (employeeRepository.existsByName(request.nombre())){
            throw new IllegalArgumentException("Employee's name already registered");
        }

        Employee employee = new Employee();
        employee.setName(request.nombre());


        Employee savedEmployee = employeeRepository.save(employee);
        return mapToResponse(savedEmployee);
    }

    @Override
    public EmpleadoResponse getById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return mapToResponse(employee);
    }

    @Override
    public List<EmpleadoResponse> findAll() {
        return employeeRepository.findAll().stream()
                .map(this::mapToResponse).toList();
    }

    @Override
    public EmpleadoResponse deactivate(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setActive(false);
        Employee updatedEmployee = employeeRepository.save(employee);

        return mapToResponse(updatedEmployee);
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
