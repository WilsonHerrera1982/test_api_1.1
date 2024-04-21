package com.api.rest.controlador;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.rest.dto.ReporteDto;
import com.api.rest.entidad.Movimiento;
import com.api.rest.servicio.MovimientoServicio;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoControlador {

    @Autowired
    private MovimientoServicio movimientoServicio;

    @PostMapping("{numeroCuenta}")
    public ResponseEntity guardarMovimiento(@PathVariable String numeroCuenta,  @RequestBody Movimiento movimiento) {
        return movimientoServicio.guardar(movimiento,numeroCuenta);
    }

    @GetMapping("/reporte/{identificacion}/{fechaInicio}/{fechaFin}")
    public ReporteDto generarReporte(@PathVariable String identificacion,
                                           @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
                                           @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin) {
        return movimientoServicio.reporte(identificacion, fechaInicio, fechaFin);
    }
}
