package com.citabella.citabellaapi.dto.cliente;

import com.citabella.citabellaapi.entity.utiles.Genero;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClienteResponse {

    private Integer idCliente;

    private String nombre;

    private String telefono;

    private Genero genero;
}
