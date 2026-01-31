package com.citabella.citabellaapi.controller.client;

import com.citabella.citabellaapi.dto.client.ClienteRequest;
import com.citabella.citabellaapi.dto.client.ClienteResponse;
import com.citabella.citabellaapi.service.interfaces.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClientService clientService;

    public ClienteController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@RequestBody ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createFull(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(clientService.getById(id));
    }
}
