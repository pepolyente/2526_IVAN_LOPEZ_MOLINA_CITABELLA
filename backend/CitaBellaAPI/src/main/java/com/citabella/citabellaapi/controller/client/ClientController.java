package com.citabella.citabellaapi.controller.client;

import com.citabella.citabellaapi.dto.client.ClientRequest;
import com.citabella.citabellaapi.dto.client.ClientResponse;
import com.citabella.citabellaapi.service.interfaces.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@RequestBody ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createFull(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(clientService.getById(id));
    }
}
