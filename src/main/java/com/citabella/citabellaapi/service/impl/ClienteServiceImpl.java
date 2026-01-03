package com.citabella.citabellaapi.service.impl;

import com.citabella.citabellaapi.dto.cliente.ClienteRequest;
import com.citabella.citabellaapi.entity.cliente.Cliente;
import com.citabella.citabellaapi.repository.ClienteRepository;
import com.citabella.citabellaapi.service.interfaces.ClienteService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente crearCliente(ClienteRequest request) {

        if (clienteRepository.existsByTelefono(request.getTelefono())) {
            throw new IllegalArgumentException("Teléfono ya registrado");
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setTelefono(request.getTelefono());
        cliente.setGenero(request.getGenero());
        cliente.setFechaNacimiento(request.getFechaNacimiento());

        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente obtenerPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }
}
