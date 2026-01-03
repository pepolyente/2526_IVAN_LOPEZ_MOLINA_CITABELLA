package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.cliente.ClienteRequest;
import com.citabella.citabellaapi.entity.cliente.Cliente;

import java.util.List;

public interface ClienteService {
    Cliente crearCliente(ClienteRequest request);

    Cliente obtenerPorId(Integer id);

    List<Cliente> listar();
}
