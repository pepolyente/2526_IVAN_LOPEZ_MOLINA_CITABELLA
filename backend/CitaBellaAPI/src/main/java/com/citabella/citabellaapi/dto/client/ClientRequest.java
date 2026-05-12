package com.citabella.citabellaapi.dto.client;

import com.citabella.citabellaapi.entity.enums.Gender;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ClientRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        LocalDate birthday,
        Gender gender
) {}
