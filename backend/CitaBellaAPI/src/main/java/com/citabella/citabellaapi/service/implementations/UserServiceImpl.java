package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.auth.UserInfoResponse;
import com.citabella.citabellaapi.dto.filter.FilterRequest;
import com.citabella.citabellaapi.dto.user.UserRequest;
import com.citabella.citabellaapi.dto.user.UserResponse;
import com.citabella.citabellaapi.dto.user.UserUpdateRequest;
import com.citabella.citabellaapi.entity.enums.AccountStatus;
import com.citabella.citabellaapi.entity.security.Role;
import com.citabella.citabellaapi.entity.security.User;
import com.citabella.citabellaapi.exception.BadRequestException;
import com.citabella.citabellaapi.exception.ResourceNotFoundException;
import com.citabella.citabellaapi.mappers.UserMapper;
import com.citabella.citabellaapi.repository.ClientRepository;
import com.citabella.citabellaapi.repository.RoleRepository;
import com.citabella.citabellaapi.repository.UserRepository;
import com.citabella.citabellaapi.repository.specifications.UserSpecification;
import com.citabella.citabellaapi.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;


    @Override
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already registered");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username already exists");
        }
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("USER role not found"));

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(role);

        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getById(Integer id) {
        return UserMapper.toResponse(
                userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }

    @Override
    public UserResponse getByEmail(String email) {
        return UserMapper.toResponse(
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }

    @Override
    public UserResponse getAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadRequestException("User not authenticated");
        }
        return getByEmail(auth.getName());
    }

    /**
     * Búsqueda paginada con filtros dinámicos:
     * - search: búsqueda LIKE por username (User no tiene campo "name")
     * - accountStatus: filtro exacto por estado de cuenta
     * <p>
     * Si search es null/blank o accountStatus es null, el predicado correspondiente se omite.
     */
    @Override
    public Page<UserResponse> findAll(Pageable pageable, AccountStatus accountStatus, FilterRequest filterRequest) {
        String search = (filterRequest != null) ? filterRequest.search() : null;

        Specification<User> spec = UserSpecification.withFilters(search, accountStatus);

        return userRepository.findAll(spec, pageable)
                .map(UserMapper::toResponse);
    }

    @Override
    public UserResponse update(Integer id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.username() != null && !request.username().isBlank()) {
            if (!request.username().equals(user.getUsername())
                    && userRepository.existsByUsername(request.username())) {
                throw new BadRequestException("Username already exists");
            }
            user.setUsername(request.username());
        }
        if (request.email() != null && !request.email().isBlank()) {
            if (!request.email().equals(user.getEmail())
                    && userRepository.existsByEmail(request.email())) {
                throw new BadRequestException("Email already registered");
            }
            user.setEmail(request.email());
        }
        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse deactivate(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getAccountStatus() == AccountStatus.LOCKED) {
            throw new BadRequestException("User is already locked");
        }
        user.setAccountStatus(AccountStatus.LOCKED);
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserInfoResponse swapRole(Integer userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        user.setRole(role);
        userRepository.save(user);

        return new UserInfoResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().getName()
        );
    }

    @Override
    public boolean hasClient(Integer userId) {
        return clientRepository.existsByUser_Id(userId);
    }
}
