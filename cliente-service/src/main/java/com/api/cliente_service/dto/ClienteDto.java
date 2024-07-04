package com.api.cliente_service.dto;

import java.io.Serializable;

public class ClienteDto implements Serializable {
    private Long id;
    private String nombre;

    public ClienteDto(Long id2, String nombre2) {
		// TODO Auto-generated constructor stub
	}

	public ClienteDto() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}