package com.api_rest_test;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.rest.entidad.Cliente;
import com.api.rest.entidad.Persona;
import com.api.rest.repositorio.ClienteRepositorio;
import com.api.rest.servicio.PersonaServicio;

@ExtendWith(MockitoExtension.class)
public class ClienteServicioTest {

	@Mock
	private ClienteRepositorio clienteRepositorio;

	@InjectMocks
	private PersonaServicio clienteServicio;

	@Test
	public void testObtenerClientePorId() {
		// Datos de prueba
		Long idCliente = 6L;
		Cliente clientePrueba = new Cliente();
		//clientePrueba.setClienteId(idCliente);

		// Simular el comportamiento del repositorio
		when(clienteRepositorio.findByClienteId(idCliente)).thenReturn(Optional.of(clientePrueba));

		Optional<Cliente> clienteObtenido = clienteServicio.obtenerXId(idCliente);

		if (clienteObtenido.isPresent() && clienteObtenido.get().getClienteId()!=null) {
			Cliente cliente = clienteObtenido.get();
			System.out.println("Cliente obtenido: " + cliente.getClienteId());
		} else {
			System.out.println("No se encontró ningún cliente con el ID: " + idCliente);
		}
	}

}
