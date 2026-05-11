package com.citabella.citabellaapi.mappers;

import com.citabella.citabellaapi.dto.client.ClientResponse;
import com.citabella.citabellaapi.entity.client.Client;

public class ClientMapper {

    public static ClientResponse toResponse(Client client) {
        if (client == null) return null;
        String linkedUsername = null;
        if (client.getUser() != null) linkedUsername = client.getUser().getUsername();

        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getPhoneNumber(),
                client.getGender(),
                linkedUsername,
                client.getActive()
        );
    }
}
