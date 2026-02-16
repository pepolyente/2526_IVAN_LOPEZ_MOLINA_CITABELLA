package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.client.ClientRequest;
import com.citabella.citabellaapi.dto.client.ClientResponse;

import java.util.List;

public interface ClientService {
    ClientResponse createFull(ClientRequest request);

    ClientResponse getById(Integer id);

    List<ClientResponse> findAll();

    ClientResponse assignUser(Integer clientId, Integer userId);

    ClientResponse unassignUser(Integer clientId);

    ClientResponse createBasic(String name, String phoneNumber);

    void linkUserAccount(Integer clientId, Integer userId);

    void unlinkUserAccount(Integer clientId);

    List<ClientResponse> findAllActive();
}
