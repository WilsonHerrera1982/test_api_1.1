package com.api.cuenta_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.cuenta_service.model.Cuenta;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
     List<Cuenta> findByClienteId(Long clienteId);
     Optional<Cuenta> findByNumeroCuenta(String numero);
}
