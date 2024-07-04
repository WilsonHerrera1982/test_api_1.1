package com.api.cliente_service.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.cliente_service.dto.ClienteDto;
import com.api.cliente_service.model.Cliente;
import com.api.cliente_service.repository.ClienteRepository;

import jakarta.annotation.PostConstruct;

@Component
public class ClienteLocalCache {

    private final ConcurrentHashMap<Long, ClienteDto> clienteCache = new ConcurrentHashMap<>();
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ClienteRepository clienteRepository;

    @PostConstruct
    public void init() {
        clienteService.cargarClientesEnCola();
        reloadCache();
    }
   

    public void reloadCache() {
        clienteCache.clear();
        List<Cliente> clientes = clienteRepository.findAll();
        for (Cliente cliente : clientes) {
            clienteCache.put(cliente.getId(), new ClienteDto(cliente.getId(), cliente.getNombre()));
        }
    }

    
}