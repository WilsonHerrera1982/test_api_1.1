package com.api.cuenta_service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClienteDto implements Serializable {
    private Long id;
    private String nombre;

    @JsonCreator
    public ClienteDto(@JsonProperty("id") Long id, @JsonProperty("nombre") String nombre) {
        this.id = id;
        this.nombre = nombre;
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