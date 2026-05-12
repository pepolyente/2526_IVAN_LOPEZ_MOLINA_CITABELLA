package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.client.ClientRequest;
import com.citabella.citabellaapi.dto.client.ClientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {
    ClientResponse createFull(ClientRequest request);

    ClientResponse getById(Integer id);

    Page<ClientResponse> findAll(Pageable pageable, Boolean active);

    ClientResponse update(Integer id, ClientRequest request);

    ClientResponse deactivate(Integer id);

    ClientResponse assignUser(Integer clientId, Integer userId);

    ClientResponse unassignUser(Integer clientId);

    ClientResponse createBasic(String name, String phoneNumber);

    void linkUserAccount(Integer clientId, Integer userId);

    void unlinkUserAccount(Integer clientId);

    List<ClientResponse> findAllActive();
}
