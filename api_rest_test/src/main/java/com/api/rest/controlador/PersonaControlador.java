package com.api.rest.controlador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.rest.entidad.Cliente;
import com.api.rest.entidad.Persona;
import com.api.rest.servicio.ClienteServicio;
import com.api.rest.servicio.PersonaServicio;

@RestController
@RequestMapping("/api/personas")
public class PersonaControlador {

   
    @Autowired
    private PersonaServicio personaServicio;
    @Autowired
    private ClienteServicio clienteServicio;
    
    
    @GetMapping
    public List<Persona> getAllClientes() {
        return personaServicio.listarPersonas();
    }

    @GetMapping("/{id}")
    public Optional<Persona> getClienteById(@PathVariable String id) {
        return personaServicio.obtenerXIdentificacion(id);
    }

   

    @PutMapping("/{id}")
    public Persona updateCliente(@PathVariable Long id, @RequestBody Persona cliente) {
        return personaServicio.updatePersona(id, cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCliente(@PathVariable String id) {
        return personaServicio.eliminar(id);
    }
    @PutMapping
    public ResponseEntity guardar(@RequestBody Persona cliente){
    	return clienteServicio.guardar(cliente);
    }
}
