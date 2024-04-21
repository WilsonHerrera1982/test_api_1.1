package com.api.rest.repositorio;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.rest.entidad.Movimiento;

@Repository
public interface MovimientoRepositorio extends JpaRepository<Movimiento, UUID> {
	@Query("SELECT m FROM Movimiento m join m.cuenta c " + " WHERE c.numeroCuenta=:numeroCuenta ORDER BY m.fecha DESC ")
	List<Movimiento> listaMovimientos(String numeroCuenta);

	@Query("SELECT m FROM Movimiento m join m.cuenta c join c.cliente cl join cl.persona p "
			+ " WHERE p.identificacion=:identificacion "
			+ " and (cast(m.fecha as string) between coalesce(cast(:fechaInicio as string), cast(m.fecha as string)) "
			+ " and coalesce(cast(:fechaFin as string), cast(m.fecha as string))) ORDER BY m.fecha DESC ")
	List<Movimiento> reporte(@Param("identificacion") String identificacion, @Param("fechaInicio") Date fechaInicio,
			@Param("fechaFin") Date fechaFin);

	@Query("SELECT m FROM Movimiento m join m.cuenta c " + " WHERE c.numeroCuenta=:numeroCuenta ORDER BY m.fecha DESC ")
	List<Movimiento> obtenerUltimoRegistro(String numeroCuenta);

	@Query("SELECT coalesce(sum(m.valor),0.00) FROM Movimiento m join m.cuenta c "
			+ " WHERE c.numeroCuenta=:numeroCuenta and m.tipoMovimiento='Retiro' and  cast(m.fecha as date)=:fecha ")
	BigDecimal maximoDiarioRetiro(String numeroCuenta, Date fecha);

}
