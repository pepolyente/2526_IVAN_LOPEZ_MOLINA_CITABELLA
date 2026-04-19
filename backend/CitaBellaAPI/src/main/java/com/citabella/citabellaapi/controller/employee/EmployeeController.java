package com.citabella.citabellaapi.controller.employee;

import com.citabella.citabellaapi.docs.ApiSecurityDocs;
import com.citabella.citabellaapi.dto.employee.EmployeeRequest;
import com.citabella.citabellaapi.dto.employee.EmployeeResponse;
import com.citabella.citabellaapi.service.interfaces.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Employees")
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(
            summary = "Create employee",
            description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get employee by ID",
            description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    @Operation(
            summary = "Get all employees",
            description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @Operation(
            summary = "Link user account to a employee",
            description = ApiSecurityDocs.ADMIN +
                    """
                            \nThis operation assigns a user to a employee and changes the user's role to EMPLOYEE and changes the account status to ACTIVE
                            """)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{employeeId}/link-user/{userId}")
    public ResponseEntity<Void> linkUserToEmployee(@PathVariable Integer employeeId, @PathVariable Integer userId) {
        employeeService.linkUserAccount(employeeId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Deactivate employee",
            description = ApiSecurityDocs.ADMIN +
                    """
                            \nThis operation changes the account status to DEACTIVATED
                            """)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{employeeId}/deactivate/")
    public ResponseEntity<EmployeeResponse> deactivate(@PathVariable Integer employeeId) {

        return ResponseEntity.ok().body(employeeService.deactivate(employeeId));
    }

    @Operation(
            summary = "Activate employee",
            description = ApiSecurityDocs.ADMIN +
                    "This operation changes the account status to ACTIVE")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{employeeId}/activate/")
    public ResponseEntity<EmployeeResponse> activate(@PathVariable Integer employeeId) {
        return ResponseEntity.ok().body(employeeService.activate(employeeId));
    }


}
