package com.api.cuenta_service.service;

import com.api.cuenta_service.dto.ClienteDto;
import com.api.cuenta_service.dto.EstadoCuentaDTO;
import com.api.cuenta_service.exception.ClienteNotFoundException;
import com.api.cuenta_service.model.Cuenta;
import com.api.cuenta_service.model.Movimiento;
import com.api.cuenta_service.repository.CuentaRepository;
import com.api.cuenta_service.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

     @Autowired
    private ClienteLocalCache clienteLocalCache;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    public EstadoCuentaDTO generarEstadoCuenta(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
         ClienteDto cliente = clienteLocalCache.getCliente(clienteId);
        if (cliente == null) {
            throw new ClienteNotFoundException("Cliente no encontrado en el caché local");
        }

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        EstadoCuentaDTO reporte = new EstadoCuentaDTO();
        reporte.setNombreCliente(cliente.getNombre());
        reporte.setCuentas(cuentas.stream().map(cuenta -> {
            EstadoCuentaDTO.CuentaReporteDTO cuentaReporte = new EstadoCuentaDTO.CuentaReporteDTO();
            cuentaReporte.setNumeroCuenta(cuenta.getNumeroCuenta());
            cuentaReporte.setTipoCuenta(cuenta.getTipoCuenta());
            cuentaReporte.setSaldoDisponible(cuenta.getSaldoInicial());

            List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaBetween(
                    cuenta.getId(), fechaInicio, fechaFin);

            cuentaReporte.setMovimientos(movimientos.stream().map(mov -> {
                EstadoCuentaDTO.MovimientoReporteDTO movReporte = new EstadoCuentaDTO.MovimientoReporteDTO();
                movReporte.setFecha(mov.getFecha());
                movReporte.setTipoMovimiento(mov.getTipoMovimiento());
                movReporte.setValor(mov.getValor());
                movReporte.setSaldoResultante(mov.getSaldo());
                return movReporte;
            }).collect(Collectors.toList()));

            return cuentaReporte;
        }).collect(Collectors.toList()));

        return reporte;
    }
}