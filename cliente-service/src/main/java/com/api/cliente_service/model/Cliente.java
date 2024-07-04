package com.api.cliente_service.model;

import jakarta.persistence.Entity;

@Entity
public class Cliente extends Persona {
    private String clienteId;
    private String contrasena;
    private boolean estado;
    // Getters y setters
	public String getClienteId() {
		return clienteId;
	}
	public void setClienteId(String clienteId) {
		this.clienteId = clienteId;
	}
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
    
}