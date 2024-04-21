package com.api.rest.servicio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.rest.entidad.Cliente;
import com.api.rest.entidad.Persona;
import com.api.rest.repositorio.ClienteRepositorio;
import com.api.rest.repositorio.PersonaRepositorio;

@Service
public class ClienteServicio {

	@Autowired
	private ClienteRepositorio clienteRepositorio;
	@Autowired
	private PersonaRepositorio personaRepositorio;

	public ResponseEntity guardar(Persona persona) {
		String mensaje = "";
		Optional<Persona> personaExistente = personaRepositorio.findByIdentificacion(persona.getIdentificacion());
		if (personaExistente.isPresent()) {
			persona = new Persona();

			mensaje = "Cliente con esta identificacion ya existe!";

		} else {

			Persona per = new Persona();
			per.setDireccion(persona.getDireccion());
			per.setEdad(persona.getEdad());
			per.setGenero(persona.getGenero());
			per.setIdentificacion(persona.getIdentificacion());
			per.setNombre(persona.getNombre());
			per.setTelefono(persona.getTelefono());
			per = personaRepositorio.save(per);
			Cliente cliente = persona.getCliente();
			cliente.setPersona(per); // Establecer la relación inversa
			clienteRepositorio.save(cliente);
			mensaje = "Registro almacenado correctamente";
			return new ResponseEntity<>(mensaje, HttpStatus.CREATED);

		}

		return ResponseEntity.ok(mensaje);

	}

}