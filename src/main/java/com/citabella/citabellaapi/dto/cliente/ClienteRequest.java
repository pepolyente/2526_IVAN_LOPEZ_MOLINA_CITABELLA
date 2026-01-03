package com.citabella.citabellaapi.dto.cliente;

import com.citabella.citabellaapi.entity.utiles.Genero;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ClienteRequest {

    private String nombre;

    private String telefono;

    private LocalDate fechaNacimiento;

    private Genero genero;

}
