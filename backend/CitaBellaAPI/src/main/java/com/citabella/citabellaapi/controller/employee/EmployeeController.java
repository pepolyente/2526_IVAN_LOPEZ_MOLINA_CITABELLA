package com.citabella.citabellaapi.controller.employee;

import com.citabella.citabellaapi.dto.employee.EmployeeRequest;
import com.citabella.citabellaapi.dto.employee.EmployeeResponse;
import com.citabella.citabellaapi.service.interfaces.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{employeeId}/link-user/{userId}")
    public ResponseEntity<Void> linkUserToEmployee(
            @PathVariable Integer employeeId,
            @PathVariable Integer userId) {
        employeeService.linkUserAccount(employeeId, userId);
        return ResponseEntity.ok().build();
    }
}
