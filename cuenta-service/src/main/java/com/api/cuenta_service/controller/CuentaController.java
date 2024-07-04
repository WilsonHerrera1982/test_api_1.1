package com.api.cuenta_service.controller;

import com.api.cuenta_service.model.Cuenta;
import com.api.cuenta_service.model.Movimiento;
import com.api.cuenta_service.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CuentaController {

	@Autowired
	private CuentaService cuentaService;

	@GetMapping("/cuentas")
	public List<Cuenta> getAllCuentas() {
		return cuentaService.getAllCuentas();
	}

	@GetMapping("/cuentas/{id}")
	public ResponseEntity<Cuenta> getCuentaById(@PathVariable Long id) {
		return cuentaService.getCuentaById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/cuentas")
	public Cuenta createCuenta(@RequestBody Cuenta cuenta) {
		return cuentaService.saveCuenta(cuenta);
	}

	@PutMapping("/cuentas/{id}")
	public ResponseEntity<Cuenta> updateCuenta(@PathVariable Long id, @RequestBody Cuenta cuentaDetails) {
		Cuenta updatedCuenta = cuentaService.updateCuenta(id, cuentaDetails);
		return new ResponseEntity<>(updatedCuenta, HttpStatus.OK);
	}

	@DeleteMapping("/cuentas/{id}")
	public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {
		cuentaService.deleteCuenta(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/cuentas/{id}/movimientos")
	public ResponseEntity<Movimiento> realizarMovimiento(@PathVariable String id, @RequestBody Movimiento movimiento) {
		Movimiento nuevoMovimiento = cuentaService.realizarMovimiento(id, movimiento);
		return ResponseEntity.ok(nuevoMovimiento);
	}

	@GetMapping("/cuentas/{id}/movimientos")
	public List<Movimiento> getMovimientosByCuenta(@PathVariable Long id) {
		return cuentaService.getMovimientosByCuentaId(id);
	}
}