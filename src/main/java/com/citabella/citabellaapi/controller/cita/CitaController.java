package com.citabella.citabellaapi.controller.cita;

import com.citabella.citabellaapi.dto.cita.CitaResponse;
import com.citabella.citabellaapi.dto.cita.CrearCitaRequest;
import com.citabella.citabellaapi.service.interfaces.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    public ResponseEntity<CitaResponse> crear(@RequestBody CrearCitaRequest request) {
        CitaResponse response = citaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
