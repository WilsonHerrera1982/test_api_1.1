package com.api.cliente_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.cliente_service.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}