package com.citabella.citabellaapi.controller.empleado;

import com.citabella.citabellaapi.dto.empleado.EmpleadoRequest;
import com.citabella.citabellaapi.dto.empleado.EmpleadoResponse;
import com.citabella.citabellaapi.service.interfaces.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @PostMapping
    public ResponseEntity<EmpleadoResponse> crear(@RequestBody EmpleadoRequest request) {
        EmpleadoResponse response = empleadoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponse> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(empleadoService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoResponse>> listar() {
        return ResponseEntity.ok(empleadoService.listar());
    }


    /*
    @PostMapping
    public EmpleadoResponse crear(@RequestBody EmpleadoRequest req) {
        Empleado e = empleadoService.crear(req.nombre(), req.puesto());
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
