package com.api.rest.servicio;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.rest.entidad.Cliente;
import com.api.rest.entidad.Cuenta;
import com.api.rest.entidad.Persona;
import com.api.rest.exepcion.CuentaNotFoundException;
import com.api.rest.repositorio.ClienteRepositorio;
import com.api.rest.repositorio.CuentaRepositorio;

@Service
public class CuentaServicio {

	@Autowired
	private CuentaRepositorio cuentaRepositorio;
	@Autowired
	private PersonaServicio personaServicio;
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	public ResponseEntity guardar(Cuenta cuenta) {
		String mensaje = "";
		Optional<Persona> persona = personaServicio.obtenerXIdentificacion(cuenta.getCliente().getPersona().getIdentificacion());
		if (persona.get().getPersonaId() != null) {
			Cliente cliente = clienteRepositorio.findByPersona(persona);
			Cuenta cuenta2 = new Cuenta();
			cuenta2.setCuentaId(cuenta.getCuentaId());
			cuenta2.setCliente(cliente);
			cuenta2.setEstado(true);
			cuenta2.setNumeroCuenta(cuenta.getNumeroCuenta());
			cuenta2.setSaldoInicial(cuenta.getSaldoInicial());
			cuenta2.setTipoCuenta(cuenta.getTipoCuenta());
			Cuenta cuenta1 = cuentaRepositorio.findByNumeroCuenta(cuenta.getNumeroCuenta()).orElse(new Cuenta());
			if (cuenta1.getCuentaId() != null) {
				cuenta2 = new Cuenta();

				mensaje = "Cuenta con #" + cuenta.getNumeroCuenta() + " ya existe!";

			} else {
				cuentaRepositorio.save(cuenta2);
				mensaje = "Registro almacenado correctamente";
			}
		} else {
			mensaje = "Cliente con identificació #" + cuenta.getCliente() + "no existe!";
		}
		return ResponseEntity.ok(mensaje);

	}
	
	public Optional<Cuenta> obtenerXNumero(String numero) {
		
		Optional<Cuenta> cuen= cuentaRepositorio.findByNumeroCuenta(numero);
		if(cuen.isPresent()) {			
			return cuen;
		}else {
			throw new CuentaNotFoundException("Cuenta con #"+numero+" no encontrada!");
		}
	}
	
	public Optional<Cuenta>editar(Cuenta cu ){		
		Optional<Cuenta> cuen = cuentaRepositorio.findByNumeroCuenta(cu.getNumeroCuenta());
		//Cliente cli=clienteRepositorio.findByClienteId()
		if(cuen.isPresent()) {	
			if(!cu.getSaldoInicial().equals(cuen.get().getSaldoInicial())) {
				throw new CuentaNotFoundException("No se puede editar saldo inicial de la cuenta proceder por movimientos!");
			}else {
				cu.setCuentaId(cuen.get().getCuentaId());
				cu.setCliente(cuen.get().getCliente());
				Cuenta cuentaGuardada = cuentaRepositorio.save(cu);
			    return Optional.ofNullable(cuentaGuardada);
			}			
		}else {
			throw new CuentaNotFoundException("Cuenta con #"+cu.getNumeroCuenta()+" no encontrada!");
		}
	}
	
	public ResponseEntity eliminar(String numero) {
		
		Optional<Cuenta> cuen = cuentaRepositorio.findByNumeroCuenta(numero);
		String mensaje="";
		if(cuen.isPresent()) {	
			cuen.get().setEstado(false);
				Cuenta cuentaGuardada = cuentaRepositorio.save(cuen.get());
				mensaje="Registro eliminado correctamente";				
				return new ResponseEntity<>(mensaje, HttpStatus.CREATED);			   
						
		}else {
			throw new CuentaNotFoundException("Cuenta con #"+numero+" no encontrada!");
		}
	}
}
