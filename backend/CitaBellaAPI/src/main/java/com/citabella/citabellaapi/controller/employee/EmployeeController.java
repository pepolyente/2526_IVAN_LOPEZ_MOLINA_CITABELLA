package com.citabella.citabellaapi.controller.employee;

import com.citabella.citabellaapi.docs.ApiSecurityDocs;
import com.citabella.citabellaapi.dto.employee.EmployeeRequest;
import com.citabella.citabellaapi.dto.employee.EmployeeResponse;
import com.citabella.citabellaapi.dto.filter.FilterRequest;
import com.citabella.citabellaapi.dto.page.PageResponse;
import com.citabella.citabellaapi.service.interfaces.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Employees")
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Create employee", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@RequestBody EmployeeRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.create(request));
    }

    @Operation(summary = "Get employee by ID", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getById(@PathVariable Integer id) {

        return ResponseEntity.ok(employeeService.getById(id));
    }

    @Operation(summary = "Get all employees (paginated, filterable by active)", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public ResponseEntity<PageResponse<EmployeeResponse>> findAll(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) Boolean active,
            @ParameterObject FilterRequest filterRequest) {

        return ResponseEntity.ok(PageResponse.from(
                employeeService.findAll(pageable, active, filterRequest)));
    }

    @Operation(summary = "Update employee", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.ok(employeeService.update(id, request));
    }

    @Operation(
            summary = "Deactivate employee (logical delete)",
            description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeResponse> deactivate(@PathVariable Integer id) {

        return ResponseEntity.ok(employeeService.deactivate(id));
    }

    @Operation(summary = "Activate employee", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<EmployeeResponse> activate(@PathVariable Integer id) {

        return ResponseEntity.ok(employeeService.activate(id));
    }

    @Operation(
            summary = "Link user account to an employee",
            description = ApiSecurityDocs.ADMIN +
                    "\nAssigns a user to an employee, changes role to EMPLOYEE and accountStatus to ACTIVE.")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{employeeId}/link-user/{userId}")
    public ResponseEntity<Void> linkUserToEmployee(
            @PathVariable Integer employeeId,
            @PathVariable Integer userId) {

        employeeService.linkUserAccount(employeeId, userId);
        return ResponseEntity.ok().build();
    }
}
