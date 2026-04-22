package com.portfolio.banco.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.portfolio.banco.entity.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaId(Long cuentaId);

    @Query("""
    SELECT COALESCE(SUM(ABS(m.valor)), 0)
    FROM Movimiento m
    WHERE m.cuenta.cliente.id = :clienteId
      AND m.fecha BETWEEN :inicio AND :fin
      AND m.tipoMovimiento = 'DEBITO'
    """)
    BigDecimal sumDebitosPorClienteEnRango(Long clienteId,
                                        LocalDateTime inicio,
                                        LocalDateTime fin);
}
