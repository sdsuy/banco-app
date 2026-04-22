package com.portfolio.banco.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.banco.entity.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    boolean existsByNumeroCuenta(String numeroCuenta);
    List<Cuenta> findByClienteId(Long clienteId);
}
