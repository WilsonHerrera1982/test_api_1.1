package com.api.rest.repositorio;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.rest.entidad.Cuenta;

@Repository
public interface CuentaRepositorio extends JpaRepository<Cuenta,UUID>{

		Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);	
}
