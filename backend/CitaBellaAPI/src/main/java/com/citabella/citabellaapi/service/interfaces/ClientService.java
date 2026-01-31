package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.client.ClienteRequest;
import com.citabella.citabellaapi.dto.client.ClienteResponse;

import java.util.List;

public interface ClientService {
    ClienteResponse createFull(ClienteRequest request);

    ClienteResponse getById(Integer id);

    List<ClienteResponse> findAll();

    ClienteResponse assignUser(Integer clientId, Integer userId);

    ClienteResponse unassignUser(Integer clientId);

    ClienteResponse createBasic(String name, String phoneNumber);
}
