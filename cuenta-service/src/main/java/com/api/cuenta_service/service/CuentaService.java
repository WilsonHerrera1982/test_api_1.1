package com.api.cuenta_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.api.cuenta_service.dto.ClienteDto;
import com.api.cuenta_service.exception.ClienteNotFoundException;
import com.api.cuenta_service.exception.CuentaNotFoundException;
import com.api.cuenta_service.exception.SaldoInsuficienteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.cuenta_service.model.Cuenta;
import com.api.cuenta_service.model.Movimiento;
import com.api.cuenta_service.repository.CuentaRepository;
import com.api.cuenta_service.repository.MovimientoRepository;

@Service
public class CuentaService {

    @Autowired
    private ClienteLocalCache clienteLocalCache;
    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    public List<Cuenta> getAllCuentas() {
        return cuentaRepository.findAll();
    }

    public Optional<Cuenta> getCuentaById(Long id) {
        return cuentaRepository.findById(id);
    }

    public Cuenta saveCuenta(Cuenta cuenta) {
        ClienteDto cliente = clienteLocalCache.getCliente(cuenta.getClienteId());
        if (cliente == null) {
            throw new ClienteNotFoundException("Cliente no encontrado en el caché local");
        }else {
        	cuenta.setEstado(true);
            return cuentaRepository.save(cuenta);
        }
    }

    public Cuenta updateCuenta(Long id, Cuenta cuentaDetails) {
        Optional<Cuenta> cuenta = cuentaRepository.findById(id);
        if (cuenta.isPresent()) {
            Cuenta existingCuenta = cuenta.get();
            existingCuenta.setNumeroCuenta(cuentaDetails.getNumeroCuenta());
            existingCuenta.setTipoCuenta(cuentaDetails.getTipoCuenta());
            existingCuenta.setSaldoInicial(cuentaDetails.getSaldoInicial());
            existingCuenta.setEstado(cuentaDetails.isEstado());
            return cuentaRepository.save(existingCuenta);
        }else {
        	throw new CuentaNotFoundException("Cuenta no encontrada en la base de datos");
        }
    }

    public void deleteCuenta(Long id) {
        cuentaRepository.deleteById(id);
    }

    public Movimiento realizarMovimiento(String cuentaNum, Movimiento movimiento) {
        Optional<Cuenta> cuenta = cuentaRepository.findByNumeroCuenta(cuentaNum);
        if (cuenta.isPresent()) {
            Cuenta cuentaActual = cuenta.get();
            BigDecimal saldoActual = cuentaActual.getSaldoInicial();
            BigDecimal nuevoSaldo = new BigDecimal(0);
            if(movimiento.getTipoMovimiento().equals("Retiro")) {
            	 if (saldoActual.compareTo(BigDecimal.ZERO) > 0 && saldoActual.compareTo(movimiento.getValor()) >= 0) { // Verifica si saldoActual es mayor o igual a movimiento.getValor()
                      nuevoSaldo = saldoActual.add(movimiento.getValor());
                     System.out.println("Retiro exitoso. Nuevo Saldo: " + nuevoSaldo);
                 } else {
                       throw new SaldoInsuficienteException("Saldo insuficiente para realizar el retiro.");
                 }
            }else {
            	  nuevoSaldo = saldoActual.add(movimiento.getValor());
            }
           

            if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Saldo no disponible");
            }

            //cuentaActual.setSaldoInicial(nuevoSaldo);
            //cuentaRepository.save(cuentaActual);

            movimiento.setCuenta(cuentaActual);
            movimiento.setSaldo(nuevoSaldo);
            return movimientoRepository.save(movimiento);
        }
        return null;
    }

    public List<Movimiento> getMovimientosByCuentaId(Long cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId);
    }
}