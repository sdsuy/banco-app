package com.portfolio.banco.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
public class Cliente extends Persona {
    @NotBlank
    @Column(name = "cliente_id", nullable = false, unique = true, length = 30)
    private String clienteId;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String contrasena;

    @NotNull
    @Column(nullable = false)
    private Boolean estado;
}
