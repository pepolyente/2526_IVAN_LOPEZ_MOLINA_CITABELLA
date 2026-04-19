package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.employee.EmployeeRequest;
import com.citabella.citabellaapi.dto.employee.EmployeeResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse create(EmployeeRequest request);

    EmployeeResponse getById(Integer id);

    List<EmployeeResponse> findAll();

    EmployeeResponse deactivate(Integer employeeId);


    void linkUserAccount(Integer employeeId, Integer userId);

    EmployeeResponse activate(Integer employeeId);
}
