package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.client.ClienteRequest;
import com.citabella.citabellaapi.dto.client.ClienteResponse;

import java.util.List;

public interface ClienteService {
    ClienteResponse crearCliente(ClienteRequest request);
    ClienteResponse obtenerPorId(Integer id);
    List<ClienteResponse> listar();
    ClienteResponse asignarUsuario(Integer idCliente, Integer idUsuario);
    ClienteResponse desasignarUsuario(Integer idCliente);
    ClienteResponse crearClienteMinimo(String nombre, String telefono);
}
