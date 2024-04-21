package com.api.rest.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.rest.entidad.Cliente;
import com.api.rest.entidad.Persona;
import com.api.rest.exepcion.PersonaNotFoundException;
import com.api.rest.repositorio.ClienteRepositorio;
import com.api.rest.repositorio.PersonaRepositorio;

@Service
public class PersonaServicio {

	@Autowired
	private PersonaRepositorio personaRepositorio;
	@Autowired
	private ClienteRepositorio clienteRepositorio;

	public Persona guardar(Persona persona) {

		return personaRepositorio.save(persona);
	}

	public List<Persona> listarPersonas() {
		List<Persona> lista= personaRepositorio.findAll();
		for(Persona per:lista) {
			  Long personaId = per.getPersonaId();
		      Optional<Cliente> clienteOptional = clienteRepositorio.findByClienteId(personaId);
		      if (clienteOptional.isPresent()) {
		          Cliente cliente = clienteOptional.get();
		        per.setCliente(cliente);
		    } 
		}
		return lista;
	}

	public ResponseEntity eliminar(String identificacion) {
		Optional<Persona> persona =personaRepositorio.findByIdentificacion(identificacion);
		String mensaje="";
		if(persona.isPresent()) {
			if(persona.get().getCliente()!=null) {
				persona.get().getCliente().setEstado(false);
			}
			mensaje="Registro eliminado correctamente";
			personaRepositorio.save(persona.get());
			return new ResponseEntity<>(mensaje, HttpStatus.CREATED);
		}else {
			throw new PersonaNotFoundException("Persona no encontrada con el ID: " + identificacion);
		}
		
	}

	public Optional<Cliente> obtenerXId(Long id) {
		return  clienteRepositorio.findByClienteId(id);
	}

	public Optional<Persona> obtenerXIdentificacion(String id) {
		return personaRepositorio.findByIdentificacion(id);
	}
	 public Persona updatePersona(Long id, Persona persona) {
	        if (personaRepositorio.existsById(id)) {
	        	Cliente cli = persona.getCliente();
	        	persona.setCliente(null);
	            persona.setPersonaId(id);
	            persona= personaRepositorio.save(persona);
	           
	            Optional<Cliente> clienteOptional = clienteRepositorio.findByClienteId(persona.getPersonaId());
			      if (clienteOptional.isPresent()) {
			          Cliente cliente = clienteOptional.get();
			          cliente.setContrasena(cli.getContrasena());
			          cliente.setEstado(cli.isEstado());
			        cliente=  clienteRepositorio.save(cliente);
			        persona.setCliente(cliente);
			    } 
	            return persona;
	        } else {
	        	throw new PersonaNotFoundException("Persona no encontrada con el ID: " + id);
	        }
	    }
}
