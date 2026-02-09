package com.citabella.citabellaapi.controller.auth;

import com.citabella.citabellaapi.config.JwtUtil;
import com.citabella.citabellaapi.dto.auth.LoginRequest;
import com.citabella.citabellaapi.dto.auth.LoginResponse;
import com.citabella.citabellaapi.dto.auth.RegisterRequest;
import com.citabella.citabellaapi.dto.auth.UserInfoResponse;
import com.citabella.citabellaapi.entity.enums.AccountStatus;
import com.citabella.citabellaapi.entity.security.Role;
import com.citabella.citabellaapi.entity.security.User;
import com.citabella.citabellaapi.exception.BadRequestException;
import com.citabella.citabellaapi.repository.RoleRepository;
import com.citabella.citabellaapi.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Operation(summary = "User login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(
                token,
                user.getUsername(),
                user.getRole().getName()
        ));
    }

    @Operation(summary = "User register")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already exists");
        }

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new BadRequestException(""));

        User user = new User();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setRole(role);
        user.setAccountStatus(AccountStatus.PENDING);

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get current user", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        return ResponseEntity.ok(new UserInfoResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().getName()
        ));
    }
}
