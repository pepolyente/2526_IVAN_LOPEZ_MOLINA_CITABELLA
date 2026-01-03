package com.citabella.citabellaapi.controller.cliente;

import com.citabella.citabellaapi.dto.cliente.ClienteRequest;
import com.citabella.citabellaapi.dto.cliente.ClienteResponse;
import com.citabella.citabellaapi.entity.cliente.Cliente;
import com.citabella.citabellaapi.service.interfaces.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@RequestBody ClienteRequest request) {

        Cliente cliente = clienteService.crearCliente(request);

        ClienteResponse response = new ClienteResponse(
                cliente.getIdCliente(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getGenero()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtener(@PathVariable Integer id) {

        Cliente cliente = clienteService.obtenerPorId(id);

        ClienteResponse response = new ClienteResponse(
                cliente.getIdCliente(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getGenero()
        );

        return ResponseEntity.ok(response);
    }
}
