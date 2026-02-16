package com.citabella.citabellaapi.controller.client;

import com.citabella.citabellaapi.docs.ApiSecurityDocs;
import com.citabella.citabellaapi.dto.client.ClientRequest;
import com.citabella.citabellaapi.dto.client.ClientResponse;
import com.citabella.citabellaapi.service.interfaces.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Clients")
@AllArgsConstructor
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Operation(
            summary = "Create client",
            description = ApiSecurityDocs.ADMIN_EMPLOYEE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createFull(request));
    }

    @Operation(
            summary = "Get client by ID",
            description = ApiSecurityDocs.ADMIN_EMPLOYEE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(clientService.getById(id));
    }

    @Operation(
            summary = "Link user account to a client",
            description = ApiSecurityDocs.ADMIN_EMPLOYEE +
                    """
                            \nThis operation assigns a user to a client and changes the user's role to CLIENT and changes the account status to ACTIVE
                            """)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PatchMapping("/{clientId}/link-user/{userId}")
    public ResponseEntity<Void> linkUserToClient(
            @PathVariable Integer clientId,
            @PathVariable Integer userId) {
        clientService.linkUserAccount(clientId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Unlink user account from client",
            description = ApiSecurityDocs.ADMIN_EMPLOYEE +
                    """
                            \nThis operation removes a user from a client and changes the user's role to USER and changes the account status to PENDING
                            """)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PatchMapping("/{clientId}/unlink-user")
    public ResponseEntity<Void> unlinkUserFromClient(@PathVariable Integer clientId) {
        clientService.unlinkUserAccount(clientId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get all clients active",
            description = ApiSecurityDocs.ADMIN_EMPLOYEE
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllActive() {
        return ResponseEntity.ok(clientService.findAllActive());
    }

}
