package com.api.cliente_service.service;
import com.api.cliente_service.dto.ClienteDto;
import com.api.cliente_service.dto.ClienteEvent;
import com.api.cliente_service.model.Cliente;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.cliente_service.repository.ClienteRepository;
import com.api.cliente_service.exception.ClienteNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    // Create
    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Read
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    // Update
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
    	 Optional<Cliente>  verificaCliente = clienteRepository.findById(id);
    	 if(!verificaCliente.isPresent()) {
    		 throw new ClienteNotFoundException("Cliente no encontrado en la base de datos");
    	 }
        Cliente clienteGuardado = clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNombre(clienteActualizado.getNombre());
                    cliente.setGenero(clienteActualizado.getGenero());
                    cliente.setEdad(clienteActualizado.getEdad());
                    cliente.setIdentificacion(clienteActualizado.getIdentificacion());
                    cliente.setDireccion(clienteActualizado.getDireccion());
                    cliente.setTelefono(clienteActualizado.getTelefono());
                    cliente.setClienteId(clienteActualizado.getClienteId());
                    cliente.setContrasena(clienteActualizado.getContrasena());
                    cliente.setEstado(clienteActualizado.isEstado());
                    return clienteRepository.save(cliente);
                })
                .orElseGet(() -> {
                    clienteActualizado.setId(id);
                    return clienteRepository.save(clienteActualizado);
                });

        ClienteEvent evento = new ClienteEvent(
                clienteGuardado.getId(),
                clienteGuardado.getNombre(),
                clienteGuardado.getGenero(),
                clienteGuardado.getEdad(),
                clienteGuardado.getIdentificacion(),
                clienteGuardado.getDireccion(),
                clienteGuardado.getTelefono(),
                clienteGuardado.isEstado()
        );
        try {
       // rabbitTemplate.convertAndSend("cliente.exchange", "cliente.routingkey", evento);
        String clienteJson = objectMapper.writeValueAsString(evento);
        rabbitTemplate.convertAndSend("cliente.exchange", "cliente.routingkey", clienteJson);
        } catch (JsonProcessingException e) {
            // Manejar la excepción
        }
        return clienteGuardado;
    }


    // Delete
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }
    
    public void cargarClientesEnCola() {
        List<Cliente> clientes = clienteRepository.findAll();
        for (Cliente cliente : clientes) {
            ClienteDto clienteDto = new ClienteDto();
            clienteDto.setId(cliente.getId());
            clienteDto.setNombre(cliente.getNombre());
            enviarClienteAQueue(clienteDto);
        }
    }
    public void enviarClienteAQueue(ClienteDto cliente) {
        try {
            String clienteJson = objectMapper.writeValueAsString(cliente);
            rabbitTemplate.convertAndSend("cliente.exchange", "cliente.routingkey", clienteJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}