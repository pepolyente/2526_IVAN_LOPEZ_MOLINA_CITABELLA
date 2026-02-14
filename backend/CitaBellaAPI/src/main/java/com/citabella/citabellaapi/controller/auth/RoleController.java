package com.citabella.citabellaapi.controller.auth;

import com.citabella.citabellaapi.docs.ApiSecurityDocs;
import com.citabella.citabellaapi.dto.user.RoleResponse;
import com.citabella.citabellaapi.entity.security.Role;
import com.citabella.citabellaapi.service.interfaces.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Role", description = "Role managment")
@AllArgsConstructor
@RestController
@RequestMapping("api/roles")
public class RoleController {

    private final RoleService roleService;

    @Operation(
            summary = "Get role by Name",
            description = ApiSecurityDocs.ADMIN_EMPLOYEE)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{name}")
    ResponseEntity<RoleResponse> getByName(@Valid @PathVariable @NotNull String name) {
        Role role = roleService.getByName(name);
        RoleResponse response = new RoleResponse(
                role.getId(),
                role.getName(),
                role.getDescription()
        );
        return ResponseEntity.ok(response);
    }

}
