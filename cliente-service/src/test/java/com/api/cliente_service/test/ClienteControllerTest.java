package com.api.cliente_service.test;

import com.api.cliente_service.controller.ClienteController;
import com.api.cliente_service.model.Cliente;
import com.api.cliente_service.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Test
    public void testCrearCliente() throws Exception {
        Cliente clienteNuevo = new Cliente();
        clienteNuevo.setId(1L);
        clienteNuevo.setNombre("Juan Pérez");
        clienteNuevo.setIdentificacion("123456789");

        when(clienteService.crearCliente(any(Cliente.class))).thenReturn(clienteNuevo);

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Juan Pérez\",\"identificacion\":\"123456789\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.identificacion").value("123456789"));
    }
    
    @Test
    public void testObtenerTodosLosClientes() throws Exception {
        /*List<Cliente> clientes = Arrays.asList(
            new Cliente(1L, "Juan Pérez", "123456789"),
            new Cliente(2L, "María López", "987654321")
        );*/
    	 Cliente cliente = new Cliente();
    	    cliente.setId(1L);
    	    cliente.setNombre("Juan Pérez");
    	    cliente.setIdentificacion("987654321");

    	    Cliente cliente2 = new Cliente();
    	    cliente2.setId(2L);  
    	    cliente2.setNombre("María López");
    	    cliente2.setIdentificacion("123456789"); 

    	    List<Cliente> clientes = new ArrayList<>();
    	    clientes.add(cliente);
    	    clientes.add(cliente2);
        when(clienteService.obtenerTodosLosClientes()).thenReturn(clientes);

        mockMvc.perform(get("/clientes"))
               .andExpect(status().isOk())
              // .andExpect(jsonPath("$", (2)))
               .andExpect(jsonPath("$[0].id").value(1))
               .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"))
               .andExpect(jsonPath("$[1].id").value(2))
               .andExpect(jsonPath("$[1].nombre").value("María López"));
    }

    @Test
    public void testObtenerClientePorId() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setIdentificacion("987654321");
        when(clienteService.obtenerClientePorId(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/clientes/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
               .andExpect(jsonPath("$.identificacion").value("987654321"));
    }

    @Test
    public void testObtenerClientePorIdNoEncontrado() throws Exception {
        when(clienteService.obtenerClientePorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/clientes/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void testActualizarCliente() throws Exception {
        Cliente clienteActualizado = new Cliente();
        
        clienteActualizado.setId(1L);
        clienteActualizado.setNombre("Juan Pérez Actualizado");
        clienteActualizado.setIdentificacion("987654321");
        when(clienteService.actualizarCliente(eq(1L), any(Cliente.class))).thenReturn(clienteActualizado);

        mockMvc.perform(put("/clientes/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"nombre\":\"Juan Pérez Actualizado\",\"identificacion\":\"987654321\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.nombre").value("Juan Pérez Actualizado"))
               .andExpect(jsonPath("$.identificacion").value("987654321"));
    }

    @Test
    public void testEliminarCliente() throws Exception {
        doNothing().when(clienteService).eliminarCliente(1L);

        mockMvc.perform(delete("/clientes/1"))
               .andExpect(status().isNoContent());
    }
}