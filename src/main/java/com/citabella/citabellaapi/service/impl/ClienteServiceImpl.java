package com.citabella.citabellaapi.service.impl;

import com.citabella.citabellaapi.dto.cliente.ClienteRequest;
import com.citabella.citabellaapi.entity.cliente.Cliente;
import com.citabella.citabellaapi.entity.seguridad.Usuario;
import com.citabella.citabellaapi.repository.ClienteRepository;
import com.citabella.citabellaapi.repository.UsuarioRepository;
import com.citabella.citabellaapi.service.interfaces.ClienteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;


    @Override
    public Cliente crearCliente(ClienteRequest request) {

        if (clienteRepository.existsByTelefono(request.telefono())) {
            throw new IllegalArgumentException("Teléfono ya registrado");
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(request.nombre());
        cliente.setTelefono(request.telefono());
        cliente.setGenero(request.genero());
        cliente.setFechaNacimiento(request.fechaNacimiento());

        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente obtenerPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente asignarUsuario(Integer idCliente, Integer idUsuario) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
        if (cliente.getUsuario() != null) {
            throw new IllegalStateException("El cliente ya tiene un usuario asignado");
        }
        if (clienteRepository.existsByUsuario_IdUsuario(idUsuario)) {
            throw new IllegalStateException("El usuario ya está asignado a otro cliente");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        cliente.setUsuario(usuario);

        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente desasignarUsuario(Integer idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
        if (cliente.getUsuario() == null) {
            throw new IllegalStateException("El cliente no tiene usuario asignado");
        }
        cliente.setUsuario(null);

        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente crearClienteMinimo(String nombre, String telefono) {
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }
        if (clienteRepository.existsByTelefono(telefono)) {
            throw new IllegalArgumentException("Teléfono ya registrado");
        }
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);

        return clienteRepository.save(cliente);
    }

}
