package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.client.ClientRequest;
import com.citabella.citabellaapi.dto.client.ClientResponse;
import com.citabella.citabellaapi.entity.client.Client;
import com.citabella.citabellaapi.entity.enums.AccountStatus;
import com.citabella.citabellaapi.entity.security.Role;
import com.citabella.citabellaapi.entity.security.User;
import com.citabella.citabellaapi.exception.BadRequestException;
import com.citabella.citabellaapi.exception.ResourceNotFoundException;
import com.citabella.citabellaapi.mappers.ClientMapper;
import com.citabella.citabellaapi.repository.ClientRepository;
import com.citabella.citabellaapi.repository.RoleRepository;
import com.citabella.citabellaapi.repository.UserRepository;
import com.citabella.citabellaapi.service.interfaces.ClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public ClientResponse createFull(ClientRequest request) {
        if (clientRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new BadRequestException("Phone number already registered");
        }
        Client client = new Client();
        client.setName(request.name());
        client.setPhoneNumber(request.phoneNumber());
        client.setGender(request.gender());
        client.setBirthday(request.birthday());
        return ClientMapper.toResponse(clientRepository.save(client));
    }

    @Override
    public ClientResponse createBasic(String name, String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new BadRequestException("Phone number is mandatory");
        }
        if (clientRepository.existsByPhoneNumber(phoneNumber)) {
            throw new BadRequestException("Phone number already registered");
        }
        Client client = new Client();
        client.setName(name);
        client.setPhoneNumber(phoneNumber);
        return ClientMapper.toResponse(clientRepository.save(client));
    }

    @Override
    public ClientResponse getById(Integer id) {
        return ClientMapper.toResponse(
                clientRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Client not found")));
    }

    @Override
    public Page<ClientResponse> findAll(Pageable pageable, Boolean active) {
        if (active != null) {
            return clientRepository.findAllByActive(active, pageable)
                    .map(ClientMapper::toResponse);
        }
        return clientRepository.findAll(pageable)
                .map(ClientMapper::toResponse);
    }

    @Override
    public List<ClientResponse> findAllActive() {
        return clientRepository.findAllByActive(true).stream()
                .map(ClientMapper::toResponse).toList();
    }

    @Override
    public ClientResponse update(Integer id, ClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        if (request.name() != null && !request.name().isBlank()) {
            client.setName(request.name());
        }
        if (request.phoneNumber() != null && !request.phoneNumber().isBlank()) {
            if (!request.phoneNumber().equals(client.getPhoneNumber())
                    && clientRepository.existsByPhoneNumber(request.phoneNumber())) {
                throw new BadRequestException("Phone number already registered");
            }
            client.setPhoneNumber(request.phoneNumber());
        }
        if (request.gender() != null) {
            client.setGender(request.gender());
        }
        if (request.birthday() != null) {
            client.setBirthday(request.birthday());
        }

        return ClientMapper.toResponse(clientRepository.save(client));
    }

    @Override
    public ClientResponse deactivate(Integer id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        if (!client.getActive()) {
            throw new BadRequestException("Client is already inactive");
        }
        client.setActive(false);
        return ClientMapper.toResponse(clientRepository.save(client));
    }

    @Override
    public ClientResponse assignUser(Integer clientId, Integer userId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        if (client.getUser() != null) {
            throw new BadRequestException("Client already has a user assigned");
        }
        if (clientRepository.existsByUser_Id(userId)) {
            throw new BadRequestException("User is already assigned to another client");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        client.setUser(user);
        return ClientMapper.toResponse(clientRepository.save(client));
    }

    @Override
    public ClientResponse unassignUser(Integer clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        if (client.getUser() == null) {
            throw new BadRequestException("Client does not have a user assigned");
        }
        client.setUser(null);
        return ClientMapper.toResponse(clientRepository.save(client));
    }

    @Override
    @Transactional
    public void linkUserAccount(Integer clientId, Integer userId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new BadRequestException("Client not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (client.getUser() != null) {
            throw new BadRequestException("Client already linked to another user");
        }
        try {
            user.assignClient(client);
        } catch (IllegalStateException e) {
            throw new BadRequestException("User already has a profile assigned");
        }

        client.setUser(user);
        clientRepository.save(client);

        Role clientRole = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new BadRequestException("CLIENT role not found"));
        user.setRole(clientRole);
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unlinkUserAccount(Integer clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        if (client.getUser() == null) {
            throw new BadRequestException("Client has no user linked");
        }
        User user = client.getUser();
        client.setUser(null);
        clientRepository.save(client);

        user.unassignProfile();
        Role noneRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new BadRequestException("USER role not found"));
        user.setRole(noneRole);
        user.setAccountStatus(AccountStatus.PENDING);
        userRepository.save(user);
    }
}
