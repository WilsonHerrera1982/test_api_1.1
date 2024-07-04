package com.api.cuenta_service.test;

import com.api.cuenta_service.controller.CuentaController;
import com.api.cuenta_service.model.Cuenta;
import com.api.cuenta_service.model.Movimiento;
import com.api.cuenta_service.service.CuentaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CuentaControllerTest {

    private MockMvc mockMvc;

   
    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private CuentaController cuentaController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(cuentaController).build();
    }

    @Test
    public void testGetAllCuentas() throws Exception {
    	Cuenta cuenta = new Cuenta();
		cuenta.setId(1L);
		cuenta.setNumeroCuenta("123456");
		cuenta.setSaldoInicial(new BigDecimal(1000.00));
		Cuenta cuenta2 = new Cuenta();
		cuenta.setId(2L);
		cuenta.setNumeroCuenta("789012");
		cuenta.setSaldoInicial(new BigDecimal(2000.00));
		List<Cuenta> cuentas = new ArrayList<>();
		cuentas.add(cuenta);
		cuentas.add(cuenta2);
        when(cuentaService.getAllCuentas()).thenReturn(cuentas);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(cuentas.size()));
    }

    @Test
    public void testGetCuentaById() throws Exception {
        Long cuentaId = 1L;
        Cuenta cuenta = new Cuenta();
		cuenta.setId(cuentaId);
		cuenta.setNumeroCuenta("123456");
		cuenta.setTipoCuenta("Corriente");
		cuenta.setSaldoInicial(new BigDecimal(1000.00));
		cuenta.setClienteId(1L);
		cuenta.setEstado(true);
        when(cuentaService.getCuentaById(eq(cuentaId))).thenReturn(Optional.of(cuenta));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cuentas/{id}", cuentaId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numeroCuenta").value(cuenta.getNumeroCuenta()));
    }

    @Test
    public void testGetCuentaByIdNotFound() throws Exception {
        when(cuentaService.getCuentaById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cuentas/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateCuenta() throws Exception {
        Cuenta newCuenta = new Cuenta();
        newCuenta.setId(null);
        newCuenta.setNumeroCuenta("123456");
        newCuenta.setSaldoInicial(new BigDecimal(1000.00));
        newCuenta.setEstado(true);
        Cuenta savedCuenta = new Cuenta();
        savedCuenta.setId(1L);
        savedCuenta.setNumeroCuenta("123456");
        savedCuenta.setSaldoInicial(new BigDecimal(1000.00));
        when(cuentaService.saveCuenta(any(Cuenta.class))).thenReturn(savedCuenta);

        mockMvc.perform(post("/api/cuentas")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"numeroCuenta\":\"123456\",\"saldoInicial\":\"1000.00\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.numeroCuenta").value("123456"));
    }

    @Test
    public void testUpdateCuenta() throws Exception {
        Cuenta updatedCuenta = new Cuenta();
        updatedCuenta.setId(1L);
        updatedCuenta.setNumeroCuenta("123456");
        updatedCuenta.setSaldoInicial(new BigDecimal(1000.00));
       
        when(cuentaService.updateCuenta(eq(1L), any(Cuenta.class))).thenReturn(updatedCuenta);

        mockMvc.perform(put("/api/cuentas/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"numeroCuenta\":\"123456\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.saldoInicial").value(1000.0));
    }

    

    @Test
    public void testDeleteCuenta() throws Exception {
        doNothing().when(cuentaService).deleteCuenta(1L);

        mockMvc.perform(delete("/api/cuentas/1"))
               .andExpect(status().isOk());
    }

    @Test
    public void testRealizarMovimiento() throws Exception {
        Movimiento movimiento = new Movimiento();
        movimiento.setId(null);
        movimiento.setTipoMovimiento("DEPOSITO");
        movimiento.setValor(new BigDecimal(500.00));
        Movimiento nuevoMovimiento = new Movimiento();
        nuevoMovimiento.setId(1L);
        nuevoMovimiento.setTipoMovimiento("DEPOSITO");
        nuevoMovimiento.setValor(new BigDecimal(500.00));
        	
        when(cuentaService.realizarMovimiento(eq("123456"), any(Movimiento.class))).thenReturn(nuevoMovimiento);

        mockMvc.perform(post("/api/cuentas/123456/movimientos")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"tipoMovimiento\":\"DEPOSITO\",\"valor\":500.00}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.tipoMovimiento").value("DEPOSITO"))
               .andExpect(jsonPath("$.valor").value(500.0));
    }

    @Test
    public void testGetMovimientosByCuenta() throws Exception {
    	Cuenta cuenta= new Cuenta();
    	cuenta.setId(1L);
        Movimiento movimiento = new Movimiento();
        movimiento.setId(1L);
        movimiento.setTipoMovimiento("DEPOSITO");
        movimiento.setValor(new BigDecimal(500.00));
        movimiento.setCuenta(cuenta);
        Movimiento nuevoMovimiento = new Movimiento();
        nuevoMovimiento.setId(2L);
        nuevoMovimiento.setTipoMovimiento("RETIRO");
        nuevoMovimiento.setValor(new BigDecimal(200.00));
        nuevoMovimiento.setCuenta(cuenta);
        List<Movimiento> movimientos = new ArrayList<>();
        movimientos.add(nuevoMovimiento);
        movimientos.add(movimiento);
        when(cuentaService.getMovimientosByCuentaId(1L)).thenReturn(movimientos);

        mockMvc.perform(get("/api/cuentas/1/movimientos"))
               .andExpect(status().isOk())               
               .andExpect(jsonPath("$[0].tipoMovimiento").value("RETIRO"))
               .andExpect(jsonPath("$[1].tipoMovimiento").value("DEPOSITO"));
    }
}
