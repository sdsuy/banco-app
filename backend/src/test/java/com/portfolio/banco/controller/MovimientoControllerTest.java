package com.portfolio.banco.controller;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.banco.AbstractIntegrationTest;
import com.portfolio.banco.dto.movimiento.MovimientoRequestDTO;
import com.portfolio.banco.entity.Cliente;
import com.portfolio.banco.entity.Cuenta;
import com.portfolio.banco.enums.Genero;
import com.portfolio.banco.enums.TipoCuenta;
import com.portfolio.banco.enums.TipoMovimiento;
import com.portfolio.banco.repository.ClienteRepository;
import com.portfolio.banco.repository.CuentaRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class MovimientoControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        cuentaRepository.deleteAll();
        clienteRepository.deleteAll();

        Cliente cliente = new Cliente();
        cliente.setNombre("Marianela Montalvo");
        cliente.setGenero(Genero.FEMENINO);
        cliente.setEdad(28);
        cliente.setIdentificacion("999999999");
        cliente.setDireccion("Amazonas y NNUU");
        cliente.setTelefono("099999999");
        cliente.setClienteId("mmontalvo");
        cliente.setContrasena("1234");
        cliente.setEstado(true);

        Cliente clienteGuardado = clienteRepository.save(cliente);

        Cuenta c = new Cuenta();
        c.setNumeroCuenta("225487");
        c.setTipoCuenta(TipoCuenta.CORRIENTE);
        c.setSaldoInicial(new BigDecimal("2000.00"));
        c.setEstado(true);
        c.setCliente(clienteGuardado);

        cuenta = cuentaRepository.save(c);
    }

    @Test
    @DisplayName("Debe rechazar débito por saldo no disponible")
    void debeRechazarDebitoPorSaldoInsuficiente() throws Exception {
        MovimientoRequestDTO dto = new MovimientoRequestDTO();
        dto.setTipoMovimiento(TipoMovimiento.DEBITO);
        dto.setValor(new BigDecimal("2500.00"));
        dto.setCuentaId(cuenta.getId());

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));
    }

    @Test
    @DisplayName("Debe rechazar débito por cupo diario excedido")
    void debeRechazarDebitoPorCupoDiarioExcedido() throws Exception {
        MovimientoRequestDTO dto1 = new MovimientoRequestDTO();
        dto1.setTipoMovimiento(TipoMovimiento.DEBITO);
        dto1.setValor(new BigDecimal("600.00"));
        dto1.setCuentaId(cuenta.getId());

        MovimientoRequestDTO dto2 = new MovimientoRequestDTO();
        dto2.setTipoMovimiento(TipoMovimiento.DEBITO);
        dto2.setValor(new BigDecimal("500.00"));
        dto2.setCuentaId(cuenta.getId());

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Cupo diario Excedido"));
    }
}
