package com.api.cliente_service.dto;

import java.io.Serializable;

public class ClienteEvent implements Serializable {
    private Long id;
    private String nombre;
    private String genero;
    private int edad;
    private String identificacion;
    private String direccion;
    private String telefono;
    private boolean estado;

    // Constructor
    public ClienteEvent(Long id, String nombre, String genero, int edad,
                        String identificacion, String direccion,
                        String telefono, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
        this.estado = estado;
    }

    public ClienteEvent(Long id, String nombre, String identificacion) {
    }

    // Getters y setters
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

    // ... getters y setters para los demás campos ...
  @Override
    public String toString() {
        return "ClienteEvent{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", genero='" + genero + '\'' +
                ", edad=" + edad +
                ", identificacion='" + identificacion + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", estado=" + estado +
                '}';
    }
}