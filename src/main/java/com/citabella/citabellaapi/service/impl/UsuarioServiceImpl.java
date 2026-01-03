package com.citabella.citabellaapi.service.impl;

import com.citabella.citabellaapi.entity.seguridad.Rol;
import com.citabella.citabellaapi.entity.seguridad.Usuario;
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
    public Usuario crearUsuario(String nombreUsuario, String email, String password) {

        if(usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email ya registrado");
        }
        if(usuarioRepository.existsByNombreUsuario(nombreUsuario)) {
            throw new IllegalArgumentException("Nombre de usuario ya existe");
        }

        Rol rol = rolRepository.findByNombre("CLIENTE_PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setEmail(email);
        usuario.setPasswordHash(passwordEncoder.encode(password));
        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario obtenerPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        String email = auth.getName();
        return obtenerPorEmail(email);
    }

    @Override
    public void cambiarRol(Integer idUsuario, String nombreRol) {
        Usuario usuario = obtenerPorId(idUsuario);
        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRol(rol);
    }

    @Override
    public boolean tieneCliente(Integer idUsuario) {
        return clienteRepository.existsByUsuario_IdUsuario(idUsuario);
    }
}
