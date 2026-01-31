package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.employee.EmployeeRequest;
import com.citabella.citabellaapi.dto.employee.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse create(EmployeeRequest request);

    EmployeeResponse getById(Integer id);

    List<EmployeeResponse> findAll();

    EmployeeResponse deactivate(Integer employeeId);
    /*List<Employee> findAllActive();*/
}
