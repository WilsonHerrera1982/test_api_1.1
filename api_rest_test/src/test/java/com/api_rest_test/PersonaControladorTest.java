package com.api_rest_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.api.rest.controlador.PersonaControlador;
import com.api.rest.entidad.Persona;
import com.api.rest.repositorio.PersonaRepositorio;
import com.api.rest.servicio.PersonaServicio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class PersonaControladorTest {

	@InjectMocks
	private PersonaControlador personaControlador;

	@Mock
	private PersonaServicio personaServicio;
	@Mock
	private PersonaRepositorio personaRepositorio;

	@Test
	public void testGetAllClientes() {

		List<Persona> personas = new ArrayList<>();
		Persona persona = new Persona();
		persona.setNombre("Maria Perez");
		persona.setGenero("FEMENINO");
		persona.setEdad(30);
		persona.setIdentificacion("123456789");
		persona.setDireccion("Dirección de prueba");
		persona.setTelefono("Teléfono de prueba");
		personas.add(persona);
		Persona persona1 = new Persona();
		persona1.setNombre("Maria Perez");
		persona1.setGenero("FEMENINO");
		persona1.setEdad(30);
		persona1.setIdentificacion("123456789");
		persona1.setDireccion("Dirección de prueba");
		persona1.setTelefono("Teléfono de prueba");
		personas.add(persona1);
		when(personaRepositorio.findAll()).thenReturn(personas);

		List<Persona> result = personaRepositorio.findAll();
		System.out.println("Resultados obtenidos:");
		for (Persona person : result) {
			     ObjectMapper objectMapper = new ObjectMapper();
	            try {
	                String jsonPersona = objectMapper.writeValueAsString(person);
	                System.out.println("Persona JSON: " + jsonPersona);
	            } catch (JsonProcessingException e) {
	                e.printStackTrace();
	            }
		}
		assertEquals(personas, result);
	}

	@Test
	public void testGetClienteById() {
		String id = "123456789";
		Persona persona = new Persona();
		persona.setNombre("Maria Perez");
		persona.setGenero("FEMENINO");
		persona.setEdad(30);
		persona.setIdentificacion("123456789");
		persona.setDireccion("Dirección de prueba");
		persona.setTelefono("Teléfono de prueba");
		when(personaRepositorio.findByIdentificacion(id)).thenReturn(Optional.of(persona));

		Optional<Persona> result = personaRepositorio.findByIdentificacion(id);
		
		 if (result.isPresent()) {
	            Persona person = result.get();
	            ObjectMapper objectMapper = new ObjectMapper();
	            try {
	                String jsonPersona = objectMapper.writeValueAsString(persona);
	                System.out.println("Persona JSON: " + jsonPersona);
	            } catch (JsonProcessingException e) {
	                e.printStackTrace();
	            }
	        } else {
	            System.out.println("No se encontró ninguna persona  " );
	        }
		assertEquals(Optional.of(persona), result);
	}

	@Test
	public void testUpdateCliente() {
		
		Persona persona = new Persona();
		persona.setNombre("Maria Perez");
		persona.setGenero("FEMENINO");
		persona.setEdad(30);
		persona.setIdentificacion("123456789");
		persona.setDireccion("Dirección de prueba");
		persona.setTelefono("Teléfono de prueba");
		when(personaRepositorio.save(persona)).thenReturn(persona);

		Persona result = personaRepositorio.save(persona);
		 if (result!=null) {
	            ObjectMapper objectMapper = new ObjectMapper();
	            try {
	                String jsonPersona = objectMapper.writeValueAsString(result);
	                System.out.println("Persona JSON: " + jsonPersona);
	            } catch (JsonProcessingException e) {
	                e.printStackTrace();
	            }
	        } else {
	            System.out.println("No se actualizo la persona: ");
	        }
		assertEquals(persona, result);
	}

	@Test
	public void testGuardar() {
		Persona persona = new Persona();
		persona.setNombre("Maria Perez");
		persona.setGenero("FEMENINO");
		persona.setEdad(30);
		persona.setIdentificacion("123456789");
		persona.setDireccion("Dirección de prueba");
		persona.setTelefono("Teléfono de prueba");
		when(personaRepositorio.save(persona)).thenReturn(persona);

		Persona result = personaRepositorio.save(persona);
		 if (result!=null) {
	            ObjectMapper objectMapper = new ObjectMapper();
	            try {
	                String jsonPersona = objectMapper.writeValueAsString(result);
	                System.out.println("Persona JSON: " + jsonPersona);
	            } catch (JsonProcessingException e) {
	                e.printStackTrace();
	            }
	        } else {
	            System.out.println("No se actualizo la persona: ");
	        }
		assertEquals(ResponseEntity.ok(persona), result);
	}
}
