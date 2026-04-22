package com.portfolio.banco.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.portfolio.banco.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByClienteId(String clienteId);

    boolean existsByClienteId(String clienteId);

    @Query("select count(c) > 0 from Cliente c where c.identificacion = :identificacion")
    boolean existsClienteByIdentificacion(String identificacion);
}
