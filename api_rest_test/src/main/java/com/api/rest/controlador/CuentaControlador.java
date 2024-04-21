package com.api.rest.controlador;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.rest.entidad.Cliente;
import com.api.rest.entidad.Cuenta;
import com.api.rest.entidad.Movimiento;
import com.api.rest.entidad.Persona;
import com.api.rest.exepcion.PersonaNotFoundException;
import com.api.rest.servicio.ClienteServicio;
import com.api.rest.servicio.CuentaServicio;
import com.api.rest.servicio.MovimientoServicio;
import com.api.rest.servicio.PersonaServicio;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaControlador {

	@Autowired
	private CuentaServicio cuentaServicio;
	@Autowired
	private MovimientoServicio movimientoServicio;
	@Autowired
	private PersonaServicio personaServicio;
	@Autowired
	private ClienteServicio clienteServicio;

	@PostMapping("{id}")
	private ResponseEntity guardar(@PathVariable String id, @RequestBody Cuenta cuenta) {
		Movimiento mov = new Movimiento();
		Cuenta cuen = new Cuenta();
		Optional<Cliente> cliente;
		Optional<Cuenta> cuentaO;
		ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		UUID uuid = UUID.randomUUID();
		String mensaje="";
		try {
			if (id.equals("") || cuenta.getNumeroCuenta().trim().equals("")
					|| cuenta.getSaldoInicial().equals("")) {
				 mensaje = "Revise la información ingresada";
				throw new Exception(mensaje);
			}
			Optional<Persona> persona = personaServicio.obtenerXIdentificacion(id);
			if (!persona.isPresent()) {
				mensaje="Persona no encontrada con el ID: " + id;
				throw new PersonaNotFoundException(mensaje);
			}else {
				cliente =personaServicio.obtenerXId(persona.get().getPersonaId());
			}
			
			cuenta.setCliente(cliente.get());
			responseEntity = cuentaServicio.guardar(cuenta);
			if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
				cuentaO = cuentaServicio.obtenerXNumero(cuenta.getNumeroCuenta());
				cuen=cuentaO.get();
				mov.setCuenta(cuen);
				mov.setFecha(new Date());
				mov.setSaldo(cuenta.getSaldoInicial());
				mov.setTipoMovimiento("Deposito");
				mov.setValor(cuenta.getSaldoInicial());
				movimientoServicio.guardarMovimiento(mov);
			}

		} catch (Exception e) {
			Logger.getLogger(CuentaControlador.class.getName()).log(Level.WARNING, null, e);
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body(mensaje);
		}
		return responseEntity;

	}

	@GetMapping("{numero}")
	private Optional<Cuenta> getCuentaById(@PathVariable String numero){
		
		return cuentaServicio.obtenerXNumero(numero);
	}
	
	@PutMapping
	private Optional<Cuenta>actualizarCuenta(@RequestBody Cuenta cuenta){
		
		return cuentaServicio.editar(cuenta);
	}
	@DeleteMapping("/{numero}")
	private ResponseEntity eliminarCuenta(@PathVariable String numero) {
		return cuentaServicio.eliminar(numero);
	}
}
