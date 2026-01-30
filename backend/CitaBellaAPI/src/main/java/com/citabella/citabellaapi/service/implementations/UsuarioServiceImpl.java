package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.user.UsuarioRequest;
import com.citabella.citabellaapi.dto.user.UsuarioResponse;
import com.citabella.citabellaapi.entity.security.Role;
import com.citabella.citabellaapi.entity.security.User;
import com.citabella.citabellaapi.repository.ClientRepository;
import com.citabella.citabellaapi.repository.RoleRepository;
import com.citabella.citabellaapi.repository.UserRepository;
import com.citabella.citabellaapi.service.interfaces.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;

    public UsuarioServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
    }

    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest request) {

        if(userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email ya registrado");
        }
        if(userRepository.existsByUsername(request.nombreUsuario())) {
            throw new IllegalArgumentException("Nombre de usuario ya existe");
        }

        Role role = roleRepository.findByName("CLIENTE_PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        User user = new User();
        user.setUsername(request.nombreUsuario());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(role);

        User creado = userRepository.save(user);
        return mapToResponse(creado);
    }

    @Override
    public UsuarioResponse obtenerPorId(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapToResponse(user);
    }

    @Override
    public UsuarioResponse obtenerPorEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapToResponse(user);
    }

    @Override
    public UsuarioResponse obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        String email = auth.getName();
        return obtenerPorEmail(email);
    }

    @Override
    public void cambiarRol(Integer idUsuario, String nombreRol) {
        User user = userRepository.findById(idUsuario)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        Role role = roleRepository.findByName(nombreRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public boolean tieneCliente(Integer idUsuario) {
        return clientRepository.existsByUser_Id(idUsuario);
    }


    private UsuarioResponse mapToResponse(User user) {
        return new UsuarioResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().getName()
        );
    }
}
