package com.api.cliente_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue clienteQueue() {
        return new Queue("cliente.queue", true);
    }

    @Bean
    public TopicExchange clienteExchange() {
        return new TopicExchange("cliente.exchange");
    }

    @Bean
    public Binding binding(Queue clienteQueue, TopicExchange clienteExchange) {
        return BindingBuilder.bind(clienteQueue).to(clienteExchange).with("cliente.routingkey");
    }
}