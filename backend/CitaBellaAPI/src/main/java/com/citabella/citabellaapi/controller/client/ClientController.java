package com.citabella.citabellaapi.controller.client;

import com.citabella.citabellaapi.docs.ApiSecurityDocs;
import com.citabella.citabellaapi.dto.client.ClientRequest;
import com.citabella.citabellaapi.dto.client.ClientResponse;
import com.citabella.citabellaapi.dto.filter.FilterRequest;
import com.citabella.citabellaapi.dto.page.PageResponse;
import com.citabella.citabellaapi.service.interfaces.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Clients")
@AllArgsConstructor
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Create client", description = ApiSecurityDocs.ADMIN_EMPLOYEE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.createFull(request));
    }

    @Operation(summary = "Get client by ID", description = ApiSecurityDocs.ADMIN_EMPLOYEE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(clientService.getById(id));
    }

    @Operation(summary = "Get all clients (paginated, filterable by active)", description = ApiSecurityDocs.ADMIN_EMPLOYEE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public ResponseEntity<PageResponse<ClientResponse>> findAll(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) Boolean active,
            @ParameterObject FilterRequest filterRequest) {
        return ResponseEntity.ok(PageResponse.from(
                clientService.findAll(pageable, active, filterRequest)));
    }

    @Operation(summary = "Update client", description = ApiSecurityDocs.ADMIN_EMPLOYEE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(clientService.update(id, request));
    }

    @Operation(
            summary = "Deactivate client (logical delete)",
            description = ApiSecurityDocs.ADMIN_EMPLOYEE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ClientResponse> deactivate(@PathVariable Integer id) {
        return ResponseEntity.ok(clientService.deactivate(id));
    }

    @Operation(
            summary = "Link user account to a client",
            description = ApiSecurityDocs.ADMIN_EMPLOYEE +
                    "\nAssigns a user to a client, changes role to CLIENT and accountStatus to ACTIVE.")
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
                    "\nRemoves the user from a client and resets the user role/status.")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PatchMapping("/{clientId}/unlink-user")
    public ResponseEntity<Void> unlinkUserFromClient(@PathVariable Integer clientId) {
        clientService.unlinkUserAccount(clientId);
        return ResponseEntity.ok().build();
    }
}
