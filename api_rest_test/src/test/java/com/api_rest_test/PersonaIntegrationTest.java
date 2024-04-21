package com.api_rest_test;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.api.rest.ApiRestTestApplication;
import com.api.rest.entidad.Persona;
import com.api.rest.repositorio.PersonaRepositorio;
import com.api.rest.servicio.PersonaServicio;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest(classes = ApiRestTestApplication.class)
@ExtendWith(MockitoExtension.class)
public class PersonaIntegrationTest {

	
	@Autowired
	private MockMvc mockMvc;

	@Mock
	private PersonaRepositorio personaRepositorio;

	@Test
	public void testGetClienteById() throws Exception {
		Persona persona = new Persona();
		
		String id = "2222222222"; 

		Mockito.when(personaRepositorio.findByIdentificacion(id)).thenReturn(Optional.of(persona));

		mockMvc.perform(get("/api/personas/" + id)).andExpect(status().isOk())
				.andExpect((ResultMatcher) content().json(new ObjectMapper().writeValueAsString(persona)));
	}
}
