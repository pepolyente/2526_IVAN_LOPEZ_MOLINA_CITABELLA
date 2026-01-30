package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.client.ClienteRequest;
import com.citabella.citabellaapi.dto.client.ClienteResponse;
import com.citabella.citabellaapi.entity.client.Client;
import com.citabella.citabellaapi.entity.security.User;
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

        Client client = new Client();
        client.setName(request.nombre());
        client.setPhoneNumber(request.telefono());
        client.setGender(request.gender());
        client.setBirthday(request.fechaNacimiento());

        Client creado = clienteRepository.save(client);
        return mapToResponse(creado);
    }

    @Override
    public ClienteResponse obtenerPorId(Integer id) {
        Client client = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return mapToResponse(client);
    }

    @Override
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream()
                .map(this::mapToResponse).toList();
    }

    @Override
    public ClienteResponse asignarUsuario(Integer idCliente, Integer idUsuario) {
        Client client = clienteRepository.findById(idCliente)
                .orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
        if (client.getUser() != null) {
            throw new IllegalStateException("El cliente ya tiene un usuario asignado");
        }
        if (clienteRepository.existsByUsuario_IdUsuario(idUsuario)) {
            throw new IllegalStateException("El usuario ya está asignado a otro cliente");
        }

        User user = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        client.setUser(user);

        Client cambiado = clienteRepository.save(client);
        return mapToResponse(cambiado);
    }

    @Override
    public ClienteResponse desasignarUsuario(Integer idCliente) {
        Client client = clienteRepository.findById(idCliente)
                .orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
        if (client.getUser() == null) {
            throw new IllegalStateException("El cliente no tiene usuario asignado");
        }
        client.setUser(null);

        Client cambiado = clienteRepository.save(client);

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
        Client client = new Client();
        client.setName(nombre);
        client.setPhoneNumber(telefono);

        Client creado = clienteRepository.save(client);

        return mapToResponse(creado);
    }


    private ClienteResponse mapToResponse(Client client) {
        return new ClienteResponse(
                client.getId(),
                client.getName(),
                client.getPhoneNumber(),
                client.getGender()
        );
    }
}
