package com.citabella.citabellaapi.controller.auth;

import com.citabella.citabellaapi.docs.ApiSecurityDocs;
import com.citabella.citabellaapi.dto.user.UserRequest;
import com.citabella.citabellaapi.dto.user.UserResponse;
import com.citabella.citabellaapi.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //todo delete method create(), already done on AuthController
    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @Operation(
            summary = "Get all users",
            description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }
}
