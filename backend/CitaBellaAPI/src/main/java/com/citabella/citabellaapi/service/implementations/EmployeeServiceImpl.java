package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.employee.EmployeeRequest;
import com.citabella.citabellaapi.dto.employee.EmployeeResponse;
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
    public EmployeeResponse create(EmployeeRequest request) {

        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Name is mandatory");
        }
        if (employeeRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Employee's name already registered");
        }

        Employee employee = new Employee();
        employee.setName(request.name());


        Employee savedEmployee = employeeRepository.save(employee);
        return mapToResponse(savedEmployee);
    }

    @Override
    public EmployeeResponse getById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return mapToResponse(employee);
    }

    @Override
    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream()
                .map(this::mapToResponse).toList();
    }

    @Override
    public EmployeeResponse deactivate(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setActive(false);
        Employee updatedEmployee = employeeRepository.save(employee);

        return mapToResponse(updatedEmployee);
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return new EmployeeResponse(
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
