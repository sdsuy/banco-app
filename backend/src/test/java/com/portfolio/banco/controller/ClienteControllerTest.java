package com.portfolio.banco.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.banco.AbstractIntegrationTest;
import com.portfolio.banco.dto.cliente.ClienteRequestDTO;
import com.portfolio.banco.enums.Genero;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class ClienteControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe crear un cliente correctamente")
    void debeCrearClienteCorrectamente() throws Exception {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNombre("Jose Lema");
        dto.setGenero(Genero.MASCULINO);
        dto.setEdad(30);
        dto.setIdentificacion("1234567890");
        dto.setDireccion("Otavalo sn y principal");
        dto.setTelefono("098254785");
        dto.setClienteId("jlema");
        dto.setContrasena("1234");
        dto.setEstado(true);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Jose Lema"))
                .andExpect(jsonPath("$.clienteId").value("jlema"))
                .andExpect(jsonPath("$.estado").value(true));
    }

    @Test
    @DisplayName("Debe rechazar cliente inválido")
    void debeRechazarClienteInvalido() throws Exception {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setGenero(Genero.MASCULINO);
        dto.setEdad(30);
        dto.setIdentificacion("1234567890");
        dto.setDireccion("Otavalo sn y principal");
        dto.setTelefono("098254785");
        dto.setClienteId("jlema");
        dto.setContrasena("1234");
        dto.setEstado(true);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").exists());
    }
}
