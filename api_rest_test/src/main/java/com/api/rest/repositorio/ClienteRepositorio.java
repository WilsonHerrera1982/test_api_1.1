package com.api.rest.repositorio;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.rest.entidad.Cliente;
import com.api.rest.entidad.Persona;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, UUID> {

	Cliente findByPersona(Optional<Persona> persona);

	Optional<Cliente> findByClienteId(Long clienteId);
}
