package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.client.ClienteRequest;
import com.citabella.citabellaapi.dto.client.ClienteResponse;
import com.citabella.citabellaapi.entity.client.Client;
import com.citabella.citabellaapi.entity.security.User;
import com.citabella.citabellaapi.repository.ClientRepository;
import com.citabella.citabellaapi.repository.UserRepository;
import com.citabella.citabellaapi.service.interfaces.ClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;


    @Override
    public ClienteResponse createFull(ClienteRequest request) {

        if (clientRepository.existsByPhoneNumber(request.telefono())) {
            throw new IllegalArgumentException("Teléfono ya registrado");
        }

        Client client = new Client();
        client.setName(request.nombre());
        client.setPhoneNumber(request.telefono());
        client.setGender(request.gender());
        client.setBirthday(request.fechaNacimiento());

        Client creado = clientRepository.save(client);
        return mapToResponse(creado);
    }

    @Override
    public ClienteResponse getById(Integer id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return mapToResponse(client);
    }

    @Override
    public List<ClienteResponse> findAll() {
        return clientRepository.findAll().stream()
                .map(this::mapToResponse).toList();
    }

    @Override
    public ClienteResponse assignUser(Integer clientId, Integer userId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
        if (client.getUser() != null) {
            throw new IllegalStateException("El cliente ya tiene un usuario asignado");
        }
        if (clientRepository.existsByUser_Id(userId)) {
            throw new IllegalStateException("El usuario ya está asignado a otro cliente");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        client.setUser(user);

        Client cambiado = clientRepository.save(client);
        return mapToResponse(cambiado);
    }

    @Override
    public ClienteResponse unassignUser(Integer clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
        if (client.getUser() == null) {
            throw new IllegalStateException("El cliente no tiene usuario asignado");
        }
        client.setUser(null);

        Client cambiado = clientRepository.save(client);

        return mapToResponse(cambiado);
    }

    @Override
    public ClienteResponse createBasic(String name, String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }
        if (clientRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Teléfono ya registrado");
        }
        Client client = new Client();
        client.setName(name);
        client.setPhoneNumber(phoneNumber);

        Client creado = clientRepository.save(client);

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
