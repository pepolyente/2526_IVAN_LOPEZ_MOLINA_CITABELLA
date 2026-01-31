package com.citabella.citabellaapi.controller.employee;

import com.citabella.citabellaapi.dto.employee.EmployeeRequest;
import com.citabella.citabellaapi.dto.employee.EmployeeResponse;
import com.citabella.citabellaapi.service.interfaces.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }


    /*
    @PostMapping
    public EmpleadoResponse crear(@RequestBody EmpleadoRequest req) {
        Empleado e = empleadoService.crear(req.name(), req.position());
        return new EmpleadoResponse(e.getIdEmpleado(), e.getNombre(), e.getPuesto(), e.getActivo());
    }

    @GetMapping
    public List<EmpleadoResponse> listar() {
        return empleadoService.listarActivos().stream()
                .map(e -> new EmpleadoResponse(e.getIdEmpleado(), e.getNombre(), e.getPuesto(), e.getActivo()))
                .toList();
    }

    @GetMapping("/{id}")
    public EmpleadoResponse obtener(@PathVariable Integer id) {
        Empleado e = empleadoService.obtenerPorId(id);
        return new EmpleadoResponse(e.getIdEmpleado(), e.getNombre(), e.getPuesto(), e.getActivo());
    }
    */
}
