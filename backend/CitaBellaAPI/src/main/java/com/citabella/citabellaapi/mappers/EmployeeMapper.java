package com.citabella.citabellaapi.mappers;

import com.citabella.citabellaapi.dto.employee.EmployeeResponse;
import com.citabella.citabellaapi.entity.employee.Employee;

public class EmployeeMapper {

    public static EmployeeResponse toResponse(Employee employee) {
        if (employee == null) return null;

        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getPosition(),
                employee.getActive()
        );
    }
}