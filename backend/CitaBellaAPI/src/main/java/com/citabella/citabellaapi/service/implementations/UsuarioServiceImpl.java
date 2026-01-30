package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.user.UsuarioRequest;
import com.citabella.citabellaapi.dto.user.UsuarioResponse;
import com.citabella.citabellaapi.entity.security.Role;
import com.citabella.citabellaapi.entity.security.User;
import com.citabella.citabellaapi.repository.ClienteRepository;
import com.citabella.citabellaapi.repository.RolRepository;
import com.citabella.citabellaapi.repository.UsuarioRepository;
import com.citabella.citabellaapi.service.interfaces.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder,ClienteRepository clienteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest request) {

        if(usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email ya registrado");
        }
        if(usuarioRepository.existsByNombreUsuario(request.nombreUsuario())) {
            throw new IllegalArgumentException("Nombre de usuario ya existe");
        }

        Role role = rolRepository.findByNombre("CLIENTE_PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        User user = new User();
        user.setUsername(request.nombreUsuario());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(role);

        User creado = usuarioRepository.save(user);
        return mapToResponse(creado);
    }

    @Override
    public UsuarioResponse obtenerPorId(Integer id) {
        User user = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapToResponse(user);
    }

    @Override
    public UsuarioResponse obtenerPorEmail(String email) {
        User user = usuarioRepository.findByEmail(email)
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
        User user = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        Role role = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        user.setRole(role);
        usuarioRepository.save(user);
    }

    @Override
    public boolean tieneCliente(Integer idUsuario) {
        return clienteRepository.existsByUsuario_IdUsuario(idUsuario);
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
