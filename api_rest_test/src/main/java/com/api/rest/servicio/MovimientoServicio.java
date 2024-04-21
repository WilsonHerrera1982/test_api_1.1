package com.api.rest.servicio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.rest.dto.CuentaDto;
import com.api.rest.dto.MovimientoDto;
import com.api.rest.dto.ReporteDto;
import com.api.rest.entidad.Cuenta;
import com.api.rest.entidad.Movimiento;
import com.api.rest.exepcion.MovimientoNotFoundException;
import com.api.rest.exepcion.ReporteNotFoundException;
import com.api.rest.repositorio.CuentaRepositorio;
import com.api.rest.repositorio.MovimientoRepositorio;

@Service
public class MovimientoServicio {

	@Autowired
	private MovimientoRepositorio movimientoRepositorio;
	@Autowired
	private CuentaRepositorio cuentaRepositorio;
	private List<Movimiento> movimientosAhorros = new ArrayList<>();
	private List<Movimiento> movimientosCorriente = new ArrayList<>();

	public ResponseEntity guardar(Movimiento movimiento, String numero) {
		String mensaje = "";
		try {
			Optional<Cuenta> cuenta = cuentaRepositorio.findByNumeroCuenta(numero);
			if (cuenta.isPresent()) {
				List<Movimiento> listaMov = movimientoRepositorio.listaMovimientos(cuenta.get().getNumeroCuenta());
				if (movimiento.getFecha() == null) {
					movimiento.setFecha(new Date());
				}
				if (movimiento.getValor().compareTo(BigDecimal.ZERO) < 0) {				    
				    mensaje = "El valor del movimiento no puede ser negativo";
				    throw new MovimientoNotFoundException(mensaje);
				}
				if (listaMov.isEmpty()) {
					if (!movimiento.getTipoMovimiento().equals("Retiro")) {
						Movimiento movimiento2 = new Movimiento();
						movimiento2.setFecha(new Date());
						movimiento2.setMovimientoId(movimiento.getMovimientoId());
						movimiento2.setTipoMovimiento(movimiento.getTipoMovimiento());
						movimiento2.setCuenta(cuenta.get());
						movimiento2.setSaldo(cuenta.get().getSaldoInicial());
						movimiento2.setValor(movimiento.getValor());
						movimientoRepositorio.save(movimiento2);
						mensaje = "Registro guardardo con exito";
					} else {
						mensaje = "Saldo Insuficiente";

					}
				} else {
					List<Movimiento> lista = listaMov.stream()
							.filter(mov -> mov.getFecha().equals(movimiento.getFecha())).collect(Collectors.toList());
					List<Movimiento> ordenar = listaMov.stream()
							.sorted(Comparator.comparing(Movimiento::getFecha).reversed()).toList();
					Movimiento movimiento2 = new Movimiento();
					movimiento2.setCuenta(listaMov.get(0).getCuenta());
					movimiento2.setFecha(movimiento.getFecha());
					movimiento2.setMovimientoId(movimiento.getMovimientoId());
					movimiento2.setTipoMovimiento(movimiento.getTipoMovimiento());
					if (movimiento.getTipoMovimiento().equals("Retiro")) {
						double suma = listaMov.stream().mapToDouble(m -> m.getValor().doubleValue()).sum();
						if (ordenar.get(0).getSaldo().compareTo(movimiento.getValor()) < 0) {
							mensaje = "Saldo no disponible";
							return ResponseEntity.ok(mensaje);
						} else {
							movimiento2.setValor(movimiento.getValor().negate());
							movimiento2.setSaldo(ordenar.get(0).getSaldo().subtract(movimiento.getValor()));
							movimientoRepositorio.save(movimiento2);
							mensaje = "Registro guardardo con exito";
						}
					} else {

						movimiento2.setValor(movimiento.getValor());
						movimiento2.setSaldo(ordenar.get(0).getSaldo().add(movimiento.getValor()));
						movimientoRepositorio.save(movimiento2);
						mensaje = "Registro guardardo con exito";
					}
				}

				return ResponseEntity.ok(mensaje);
			} else {
				mensaje = "Cuenta con #" + numero + " no existe!";
				throw new MovimientoNotFoundException(mensaje);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensaje);
		}
	}

	public ReporteDto reporte(String identificacion, Date fechaInicio, Date fechaFin) {
		List<Movimiento> listaM = movimientoRepositorio.reporte(identificacion, fechaInicio, fechaFin);
		List<Movimiento> movimientosAhorros = new ArrayList<>();
		List<Movimiento> movimientosCorriente = new ArrayList<>();
		String mensaje = "";
		try {
			if (!listaM.isEmpty()) {

				movimientosAhorros = listaM.stream()
						.filter(movimiento -> movimiento.getCuenta().getTipoCuenta().equals("AHORROS"))
						.collect(Collectors.toList());

				movimientosCorriente = listaM.stream()
						.filter(movimiento -> movimiento.getCuenta().getTipoCuenta().equals("CORRIENTE"))
						.collect(Collectors.toList());

				ReporteDto reporteDto = new ReporteDto();
				BigDecimal saldoDisponibleA = null;

				reporteDto.setCuentas(new ArrayList());

				for (Movimiento a : listaM) {
					CuentaDto cuentaDto = new CuentaDto();
					reporteDto.setCliente(a.getCuenta().getCliente().getPersona().getNombre());
					reporteDto.setFecha(new Date());

					List<String> numerosCuentaAgregados = reporteDto.getCuentas().stream()
							.map(CuentaDto::getNumeroCuenta).collect(Collectors.toList());

					if (!numerosCuentaAgregados.contains(a.getCuenta().getNumeroCuenta())) {
						cuentaDto.setNumeroCuenta(a.getCuenta().getNumeroCuenta());
						cuentaDto.setTipoCuenta(a.getCuenta().getTipoCuenta());
						reporteDto.getCuentas().add(cuentaDto);
					}

				}

				if (!movimientosAhorros.isEmpty()) {
					movimientosAhorros.sort((m1, m2) -> m2.getMovimientoId().compareTo(m1.getMovimientoId()));
					Optional<Movimiento> ultimoMovimientoOptA = movimientosAhorros.stream().findFirst();
					Movimiento ultimoMovimientoA = ultimoMovimientoOptA.get();
					saldoDisponibleA = ultimoMovimientoA.getSaldo();
					for (Movimiento a : movimientosAhorros) {
						Optional<CuentaDto> cuentaAhorrosOptional = reporteDto.getCuentas().stream()
								.filter(cuenta -> cuenta.getTipoCuenta().equals("AHORROS")).findFirst();
						CuentaDto cuentaA = cuentaAhorrosOptional.get();
						MovimientoDto movDto = new MovimientoDto();
						movDto.setFecha(a.getFecha());
						movDto.setSaldo(a.getSaldo());
						movDto.setTipoMovimiento(a.getTipoMovimiento());
						movDto.setValor(a.getValor());

						cuentaA.setNumeroCuenta(a.getCuenta().getNumeroCuenta());
						cuentaA.setSaldoDisponible(saldoDisponibleA);
						cuentaA.setTipoCuenta(a.getCuenta().getTipoCuenta());
						// reporteDto.getCuentas().add(cuentaA);
						cuentaA.getMovimientos().add(movDto);
						// cuentaDto.getMovimientos().add(movDto);
					}
				}

				if (!movimientosCorriente.isEmpty()) {
					Optional<Movimiento> ultimoMovimientoOptC = movimientosCorriente.stream().findFirst();
					Movimiento ultimoMovimientoC = ultimoMovimientoOptC.get();
					BigDecimal saldoDisponibleC = ultimoMovimientoC.getSaldo();
					for (Movimiento c : movimientosCorriente) {
						Optional<CuentaDto> cuentaCorrienteOptional = reporteDto.getCuentas().stream()
								.filter(cuenta -> cuenta.getTipoCuenta().equals("CORRIENTE")).findFirst();
						MovimientoDto movDto = new MovimientoDto();
						CuentaDto cuentaC = cuentaCorrienteOptional.get();
						movDto.setFecha(c.getFecha());
						movDto.setSaldo(c.getSaldo());
						movDto.setTipoMovimiento(c.getTipoMovimiento());
						movDto.setValor(c.getValor());

						cuentaC.setNumeroCuenta(c.getCuenta().getNumeroCuenta());
						cuentaC.setSaldoDisponible(saldoDisponibleC);
						cuentaC.setTipoCuenta(c.getCuenta().getTipoCuenta());
						// reporteDto.getCuentas().add(cuentaA);
						cuentaC.getMovimientos().add(movDto);
						// cuentaDto.getMovimientos().add(movDto);
					}
				}

				List<CuentaDto> cuentas = reporteDto.getCuentas();
				Set<CuentaDto> cuentasSinDuplicadosSet = new HashSet<>(cuentas);
				List<CuentaDto> cuentasSinDuplicados = cuentasSinDuplicadosSet.stream().collect(Collectors.toList());
				reporteDto.setCuentas(cuentasSinDuplicados);
				return reporteDto;
			} else {
				mensaje = "Cliente no registra movimientos";
				throw new ReporteNotFoundException(mensaje);
			}

		} catch (Exception e) {
			throw new ReporteNotFoundException(mensaje);
		}
	}

	public void guardarMovimiento(Movimiento mov) {
		movimientoRepositorio.save(mov);
	}

}
