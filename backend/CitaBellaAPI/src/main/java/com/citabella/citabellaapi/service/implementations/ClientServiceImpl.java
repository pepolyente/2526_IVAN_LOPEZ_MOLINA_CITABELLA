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
            throw new IllegalArgumentException("Phone number already registered");
        }

        Client client = new Client();
        client.setName(request.nombre());
        client.setPhoneNumber(request.telefono());
        client.setGender(request.gender());
        client.setBirthday(request.fechaNacimiento());

        Client createdClient = clientRepository.save(client);
        return mapToResponse(createdClient);
    }

    @Override
    public ClienteResponse getById(Integer id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
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
                .orElseThrow(() -> new RuntimeException("Client not found"));
        if (client.getUser() != null) {
            throw new IllegalStateException("Client already has a user assigned");
        }
        if (clientRepository.existsByUser_Id(userId)) {
            throw new IllegalStateException("User is already assigned to another client");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        client.setUser(user);

        Client changedClient = clientRepository.save(client);
        return mapToResponse(changedClient);
    }

    @Override
    public ClienteResponse unassignUser(Integer clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        if (client.getUser() == null) {
            throw new IllegalStateException("Client does not have a user assigned");
        }
        client.setUser(null);

        Client updatedClient = clientRepository.save(client);

        return mapToResponse(updatedClient);
    }

    @Override
    public ClienteResponse createBasic(String name, String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is mandatory");
        }
        if (clientRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number already registered");
        }
        Client client = new Client();
        client.setName(name);
        client.setPhoneNumber(phoneNumber);

        Client createdClient = clientRepository.save(client);

        return mapToResponse(createdClient);
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
