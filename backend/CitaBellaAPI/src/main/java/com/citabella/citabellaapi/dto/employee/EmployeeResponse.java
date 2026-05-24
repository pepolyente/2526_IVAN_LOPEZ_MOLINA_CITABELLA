package com.citabella.citabellaapi.dto.employee;

public record EmployeeResponse(
        Integer id,
        String name,
        String position,
        Boolean active
) {}
