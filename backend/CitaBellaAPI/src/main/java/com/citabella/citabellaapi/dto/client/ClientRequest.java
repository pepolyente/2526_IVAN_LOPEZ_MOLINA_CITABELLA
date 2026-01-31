package com.citabella.citabellaapi.dto.client;

import com.citabella.citabellaapi.entity.enums.Gender;

import java.time.LocalDate;

public record ClientRequest(
        String name,
        String phoneNumber,
        Gender gender,
        LocalDate birthday
) {}
