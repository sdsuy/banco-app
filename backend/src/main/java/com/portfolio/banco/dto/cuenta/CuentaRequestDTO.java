package com.portfolio.banco.dto.cuenta;

import java.math.BigDecimal;

import com.portfolio.banco.enums.TipoCuenta;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CuentaRequestDTO {

    @NotBlank
    private String numeroCuenta;

    @NotNull
    private TipoCuenta tipoCuenta;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal saldoInicial;

    @NotNull
    private Boolean estado;

    @NotNull
    private Long clienteId;
}
