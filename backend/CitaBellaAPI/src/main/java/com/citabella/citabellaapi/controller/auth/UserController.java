package com.citabella.citabellaapi.controller.auth;

import com.citabella.citabellaapi.docs.ApiSecurityDocs;
import com.citabella.citabellaapi.dto.auth.UserInfoResponse;
import com.citabella.citabellaapi.dto.filter.FilterRequest;
import com.citabella.citabellaapi.dto.page.PageResponse;
import com.citabella.citabellaapi.dto.user.UserRequest;
import com.citabella.citabellaapi.dto.user.UserResponse;
import com.citabella.citabellaapi.dto.user.UserUpdateRequest;
import com.citabella.citabellaapi.entity.enums.AccountStatus;
import com.citabella.citabellaapi.service.interfaces.UserService;
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


@AllArgsConstructor
@Tag(name = "Users")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create user (admin)", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(request));
    }

    @Operation(summary = "Get all users (paginated, filterable by accountStatus)", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> findAll(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) AccountStatus accountStatus,
            @ParameterObject FilterRequest filterRequest) {

        return ResponseEntity.ok(PageResponse.from(
                userService.findAll(pageable, accountStatus, filterRequest)));
    }

    @Operation(summary = "Get user by ID", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Integer id) {

        return ResponseEntity.ok(userService.getById(id));
    }

    @Operation(summary = "Update user", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody UserUpdateRequest request) {

        return ResponseEntity.ok(userService.update(id, request));
    }

    @Operation(
            summary = "Deactivate user (logical delete → LOCKED)",
            description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deactivate(@PathVariable Integer id) {

        return ResponseEntity.ok(userService.deactivate(id));
    }

    @Operation(summary = "Activate user (unlock)", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserResponse> activate(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.activate(id));
    }

    @Operation(summary = "Swap role of a user", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/swap-role/{name}")
    public ResponseEntity<UserInfoResponse> swapRole(
            @PathVariable Integer id,
            @PathVariable String name) {

        return ResponseEntity.ok(userService.swapRole(id, name));
    }
}
