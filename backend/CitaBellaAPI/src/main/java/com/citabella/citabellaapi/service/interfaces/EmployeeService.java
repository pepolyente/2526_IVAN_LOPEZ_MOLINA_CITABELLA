package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.employee.EmployeeRequest;
import com.citabella.citabellaapi.dto.employee.EmployeeResponse;
import com.citabella.citabellaapi.dto.filter.FilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeResponse create(EmployeeRequest request);

    EmployeeResponse getById(Integer id);

    Page<EmployeeResponse> findAll(Pageable pageable, Boolean active, FilterRequest filterRequest);

    EmployeeResponse update(Integer id, EmployeeRequest request);

    EmployeeResponse deactivate(Integer employeeId);

    EmployeeResponse activate(Integer employeeId);

    void linkUserAccount(Integer employeeId, Integer userId);
}
