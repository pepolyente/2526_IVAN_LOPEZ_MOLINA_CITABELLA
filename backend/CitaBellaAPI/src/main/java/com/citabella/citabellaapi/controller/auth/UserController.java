package com.citabella.citabellaapi.controller.auth;

import com.citabella.citabellaapi.docs.ApiSecurityDocs;
import com.citabella.citabellaapi.dto.auth.UserInfoResponse;
import com.citabella.citabellaapi.dto.user.UserResponse;
import com.citabella.citabellaapi.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Tag(name = "Users")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get all users",
            description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Operation(summary = "Get current user")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{id}/swap-role/{name}")
    public ResponseEntity<UserInfoResponse> swapRol(@PathVariable Integer id, @PathVariable String name) {
        UserInfoResponse response = userService.swapRole(id, name);
        return ResponseEntity.ok(response);
    }
}
