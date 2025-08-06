package com.example.ejercicio.controller;

import com.example.ejercicio.dto.ClienteRequestDTO;
import com.example.ejercicio.dto.ClienteResponseDTO;
import com.example.ejercicio.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClienteRequestDTO clienteRequestDTO;
    private ClienteResponseDTO clienteResponseDTO;

    @BeforeEach
    void setUp() {
        clienteRequestDTO = new ClienteRequestDTO();
        clienteRequestDTO.setNombre("Juan Pérez");
        clienteRequestDTO.setGenero("MASCULINO");
        clienteRequestDTO.setEdad(30);
        clienteRequestDTO.setIdentificacion("1234567890");
        clienteRequestDTO.setDireccion("Calle 123, Ciudad");
        clienteRequestDTO.setTelefono("555-1234");
        clienteRequestDTO.setClienteId("CLI001");
        clienteRequestDTO.setContrasena("password123");
        clienteRequestDTO.setEstado(true);

        clienteResponseDTO = new ClienteResponseDTO(
                1L,
                "Juan Pérez",
                "MASCULINO",
                30,
                "1234567890",
                "Calle 123, Ciudad",
                "555-1234",
                "CLI001",
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void crearCliente_DeberiaRetornar201() throws Exception {
        // Arrange
        when(clienteService.createClient(any(ClienteRequestDTO.class))).thenReturn(clienteResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.clienteId").value("CLI001"))
                .andExpect(jsonPath("$.identificacion").value("1234567890"))
                .andExpect(jsonPath("$.estado").value(true));
    }

    @Test
    void crearCliente_DeberiaRetornar400CuandoNombreEsVacio() throws Exception {
        // Arrange
        clienteRequestDTO.setNombre("");

        // Act & Assert
        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearCliente_DeberiaRetornar400CuandoClienteIdEsVacio() throws Exception {
        // Arrange
        clienteRequestDTO.setClienteId("");

        // Act & Assert
        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerClientePorId_DeberiaRetornar200() throws Exception {
        // Arrange
        when(clienteService.getClientById(1L)).thenReturn(clienteResponseDTO);

        // Act & Assert
        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.clienteId").value("CLI001"));
    }

    @Test
    void obtenerTodosLosClientes_DeberiaRetornar200() throws Exception {
        // Arrange
        List<ClienteResponseDTO> clientes = Arrays.asList(clienteResponseDTO);
        when(clienteService.getAllClients()).thenReturn(clientes);

        // Act & Assert
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));
    }

    @Test
    void obtenerClientePorClienteId_DeberiaRetornar200() throws Exception {
        // Arrange
        when(clienteService.getClientByClienteId("CLI001")).thenReturn(clienteResponseDTO);

        // Act & Assert
        mockMvc.perform(get("/clientes/cliente-id/CLI001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value("CLI001"))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));
    }

    @Test
    void actualizarCliente_DeberiaRetornar200() throws Exception {
        // Arrange
        clienteRequestDTO.setNombre("Juan Pérez Actualizado");
        clienteResponseDTO = new ClienteResponseDTO(
                1L,
                "Juan Pérez Actualizado",
                "MASCULINO",
                30,
                "1234567890",
                "Calle 123, Ciudad",
                "555-1234",
                "CLI001",
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(clienteService.updateClient(eq(1L), any(ClienteRequestDTO.class))).thenReturn(clienteResponseDTO);

        // Act & Assert
        mockMvc.perform(put("/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez Actualizado"));
    }

    @Test
    void cambiarEstadoCliente_DeberiaRetornar200() throws Exception {
        // Arrange
        when(clienteService.activateClient(1L)).thenReturn(clienteResponseDTO);

        // Act & Assert
        mockMvc.perform(patch("/clientes/1/activar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value(true));
    }

    @Test
    void eliminarCliente_DeberiaRetornar204() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerClientesActivos_DeberiaRetornar200() throws Exception {
        // Arrange
        List<ClienteResponseDTO> clientesActivos = Arrays.asList(clienteResponseDTO);
        when(clienteService.getActiveClients()).thenReturn(clientesActivos);

        // Act & Assert
        mockMvc.perform(get("/clientes/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].estado").value(true));
    }

    @Test
    void buscarClientes_DeberiaRetornar200() throws Exception {
        // Arrange
        List<ClienteResponseDTO> clientesEncontrados = Arrays.asList(clienteResponseDTO);
        when(clienteService.searchClients("Juan")).thenReturn(clientesEncontrados);

        // Act & Assert
        mockMvc.perform(get("/clientes/buscar")
                .param("busqueda", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));
    }
}
