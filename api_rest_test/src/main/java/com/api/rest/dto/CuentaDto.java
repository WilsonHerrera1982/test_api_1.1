package com.api.rest.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CuentaDto {

	private String numeroCuenta;
	private String tipoCuenta;
	private BigDecimal saldoDisponible;
	private List<MovimientoDto> movimientos;	
	
	 public CuentaDto() {
	        this.movimientos = new ArrayList<>();
	    }
	public BigDecimal getSaldoDisponible() {
		return saldoDisponible;
	}
	public void setSaldoDisponible(BigDecimal saldoDisponible) {
		this.saldoDisponible = saldoDisponible;
	}
	public List<MovimientoDto> getMovimientos() {
		return movimientos;
	}
	public void setMovimientos(List<MovimientoDto> movimientos) {
		this.movimientos = movimientos;
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
