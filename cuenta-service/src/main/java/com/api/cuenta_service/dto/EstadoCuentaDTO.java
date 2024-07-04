package com.api.cuenta_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class EstadoCuentaDTO {
    private String nombreCliente;
    private List<CuentaReporteDTO> cuentas;

    // Getters y setters

    public List<CuentaReporteDTO> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<CuentaReporteDTO> cuentas) {
        this.cuentas = cuentas;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public static class CuentaReporteDTO {
        private String numeroCuenta;
        private String tipoCuenta;
        private BigDecimal saldoDisponible;
        private List<MovimientoReporteDTO> movimientos;

        // Getters y setters

        public List<MovimientoReporteDTO> getMovimientos() {
            return movimientos;
        }

        public void setMovimientos(List<MovimientoReporteDTO> movimientos) {
            this.movimientos = movimientos;
        }

        public BigDecimal getSaldoDisponible() {
            return saldoDisponible;
        }

        public void setSaldoDisponible(BigDecimal saldoDisponible) {
            this.saldoDisponible = saldoDisponible;
        }

        public String getNumeroCuenta() {
            return numeroCuenta;
        }

        public void setNumeroCuenta(String numeroCuenta) {
            this.numeroCuenta = numeroCuenta;
        }

        public String getTipoCuenta() {
            return tipoCuenta;
        }

        public void setTipoCuenta(String tipoCuenta) {
            this.tipoCuenta = tipoCuenta;
        }
    }

    public static class MovimientoReporteDTO {
        private LocalDate fecha;
        private String tipoMovimiento;
        private BigDecimal valor;
        private BigDecimal saldoResultante;

        public BigDecimal getSaldoResultante() {
            return saldoResultante;
        }

        public void setSaldoResultante(BigDecimal saldoResultante) {
            this.saldoResultante = saldoResultante;
        }

        public BigDecimal getValor() {
            return valor;
        }

        public void setValor(BigDecimal valor) {
            this.valor = valor;
        }
// Getters y setters

        public LocalDate getFecha() {
            return fecha;
        }

        public void setFecha(LocalDate fecha) {
            this.fecha = fecha;
        }

        public String getTipoMovimiento() {
            return tipoMovimiento;
        }

        public void setTipoMovimiento(String tipoMovimiento) {
            this.tipoMovimiento = tipoMovimiento;
        }
    }
}
