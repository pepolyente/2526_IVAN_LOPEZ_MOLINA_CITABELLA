package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.employee.EmpleadoRequest;
import com.citabella.citabellaapi.dto.employee.EmpleadoResponse;

import java.util.List;

public interface EmployeeService {
    EmpleadoResponse create(EmpleadoRequest request);

    EmpleadoResponse getById(Integer id);

    List<EmpleadoResponse> findAll();

    EmpleadoResponse deactivate(Integer employeeId);
    /*List<Employee> findAllActive();*/
}
