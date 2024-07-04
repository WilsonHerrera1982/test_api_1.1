/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.api.cuenta_service.listener;

import com.api.cuenta_service.dto.ClienteDto;
import com.api.cuenta_service.service.ClienteListener;
import com.api.cuenta_service.service.ClienteLocalCache;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wilson
 */
@Service
public class ClienteEventListener {

	private static final Logger logger = LoggerFactory.getLogger(ClienteListener.class);
    @Autowired
    private ClienteLocalCache clienteLocalCache;

    @Autowired
    private ObjectMapper objectMapper;
    /*@RabbitListener(queues = "cliente.queue")
    public void recibirEventoCliente(ClienteEvent evento) {
        // Actualizar la caché local con la información del cliente
        clienteLocalCache.actualizarCliente(
            evento.getId(),
            evento.getNombre()
        );
    }*/
    @RabbitListener(queues = "cliente.queue")
    public void recibirEventoCliente(Message message) {
        try {
            String messageBody = new String(message.getBody());
            logger.info("Mensaje recibido: {}", messageBody);

            ClienteDto cliente = objectMapper.readValue(messageBody, ClienteDto.class);
            
            clienteLocalCache.actualizarCliente(cliente.getId(), cliente.getNombre());
            
            logger.info("Cliente procesado: {}", cliente);
        } catch (Exception e) {
            logger.error("Error al procesar el mensaje", e);
        }
    }
}