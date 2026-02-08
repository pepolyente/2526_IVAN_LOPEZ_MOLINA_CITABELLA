package com.citabella.citabellaapi.controller.client;

import com.citabella.citabellaapi.dto.client.ClientRequest;
import com.citabella.citabellaapi.dto.client.ClientResponse;
import com.citabella.citabellaapi.service.interfaces.ClientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createFull(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(clientService.getById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PatchMapping("/{clientId}/link-user/{userId}")
    public ResponseEntity<Void> linkUserToClient(
            @PathVariable Integer clientId,
            @PathVariable Integer userId) {
        clientService.linkUserAccount(clientId, userId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PatchMapping("/{clientId}/unlink-user")
    public ResponseEntity<Void> unlinkUserFromClient(@PathVariable Integer clientId) {
        clientService.unlinkUserAccount(clientId);
        return ResponseEntity.ok().build();
    }

}
