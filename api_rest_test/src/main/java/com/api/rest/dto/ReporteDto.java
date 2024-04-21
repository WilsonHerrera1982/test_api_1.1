package com.api.rest.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.api.rest.entidad.Movimiento;

public class ReporteDto {

	private Date fecha;
	private String cliente;
	private List<CuentaDto> cuentas = new ArrayList();
	
	public List<CuentaDto> getCuentas() {
		return cuentas;
	}
	public void setCuentas(List<CuentaDto> cuentas) {
		this.cuentas = cuentas;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	
	
}
