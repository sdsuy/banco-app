package com.portfolio.banco.dto.cliente;

import com.portfolio.banco.enums.Genero;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClienteResponseDTO {

    private Long id;
    private String nombre;
    private Genero genero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;

    private String clienteId;
    private Boolean estado;
}
