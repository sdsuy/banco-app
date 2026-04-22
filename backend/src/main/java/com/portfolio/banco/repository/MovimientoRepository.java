package com.portfolio.banco.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.banco.entity.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
}
