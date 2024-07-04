/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.api.cuenta_service.service;

/**
 *
 * @author Wilson
 */
import com.api.cuenta_service.dto.ClienteDto;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClienteLocalCache {

    private final ConcurrentHashMap<Long, ClienteDto> clienteCache = new ConcurrentHashMap<>();

    public void actualizarCliente(Long id, String nombre) {
        ClienteDto clienteDTO = new ClienteDto(id, nombre);
        clienteCache.put(id, clienteDTO);
    }

    public ClienteDto getCliente(Long id) {
        return clienteCache.get(id);
    }
}