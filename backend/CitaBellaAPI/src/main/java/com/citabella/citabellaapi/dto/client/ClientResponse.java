package com.citabella.citabellaapi.dto.client;

import com.citabella.citabellaapi.entity.enums.Gender;

public record ClientResponse(
        Integer id,
        String name,
        String phoneNumber,
        Gender gender,
        String linkedUsername,
        Boolean active
) {}
