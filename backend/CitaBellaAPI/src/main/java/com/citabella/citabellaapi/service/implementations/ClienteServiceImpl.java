package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.client.ClienteRequest;
import com.citabella.citabellaapi.dto.client.ClienteResponse;
import com.citabella.citabellaapi.entity.client.Cliente;
import com.citabella.citabellaapi.entity.security.Usuario;
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
    public ClienteResponse crearCliente(ClienteRequest request) {

        if (clienteRepository.existsByTelefono(request.telefono())) {
            throw new IllegalArgumentException("Teléfono ya registrado");
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(request.nombre());
        cliente.setTelefono(request.telefono());
        cliente.setGender(request.gender());
        cliente.setFechaNacimiento(request.fechaNacimiento());

        Cliente creado = clienteRepository.save(cliente);
        return mapToResponse(creado);
    }

    @Override
    public ClienteResponse obtenerPorId(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return mapToResponse(cliente);
    }

    @Override
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream()
                .map(this::mapToResponse).toList();
    }

    @Override
    public ClienteResponse asignarUsuario(Integer idCliente, Integer idUsuario) {
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

        Cliente cambiado = clienteRepository.save(cliente);
        return mapToResponse(cambiado);
    }

    @Override
    public ClienteResponse desasignarUsuario(Integer idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
        if (cliente.getUsuario() == null) {
            throw new IllegalStateException("El cliente no tiene usuario asignado");
        }
        cliente.setUsuario(null);

        Cliente cambiado = clienteRepository.save(cliente);

        return mapToResponse(cambiado);
    }

    @Override
    public ClienteResponse crearClienteMinimo(String nombre, String telefono) {
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }
        if (clienteRepository.existsByTelefono(telefono)) {
            throw new IllegalArgumentException("Teléfono ya registrado");
        }
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);

        Cliente creado = clienteRepository.save(cliente);

        return mapToResponse(creado);
    }


    private ClienteResponse mapToResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getIdCliente(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getGender()
        );
    }
}
