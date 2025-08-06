package com.example.ejercicio.service.impl;

import com.example.ejercicio.dto.ClienteRequestDTO;
import com.example.ejercicio.dto.ClienteResponseDTO;
import com.example.ejercicio.exception.DuplicateResourceException;
import com.example.ejercicio.exception.ResourceNotFoundException;
import com.example.ejercicio.model.Cliente;
import com.example.ejercicio.repository.ClienteRepository;
import com.example.ejercicio.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteRequestDTO clienteRequestDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(
                "Juan Pérez",
                "MASCULINO",
                30,
                "1234567890",
                "Calle 123, Ciudad",
                "555-1234",
                "CLI001",
                "password123"
        );
        cliente.setId(1L);
        cliente.setEstado(true);
        cliente.setFechaCreacion(LocalDateTime.now());
        cliente.setFechaActualizacion(LocalDateTime.now());

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
    }

    @Test
    void crearCliente_DeberiaCrearClienteExitosamente() {
        // Arrange
        when(clienteRepository.existsByClienteId("CLI001")).thenReturn(false);
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        ClienteResponseDTO resultado = clienteService.createClient(clienteRequestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals("CLI001", resultado.getClienteId());
        assertEquals("1234567890", resultado.getIdentificacion());
        assertTrue(resultado.getEstado());

        verify(clienteRepository).existsByClienteId("CLI001");
        verify(clienteRepository).existsByIdentificacion("1234567890");
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void crearCliente_DeberiaLanzarExcepcionCuandoClienteIdYaExiste() {
        // Arrange
        when(clienteRepository.existsByClienteId("CLI001")).thenReturn(true);

        // Act & Assert
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> clienteService.createClient(clienteRequestDTO)
        );

        assertEquals("Ya existe un cliente con el clienteId: CLI001", exception.getMessage());
        verify(clienteRepository).existsByClienteId("CLI001");
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void crearCliente_DeberiaLanzarExcepcionCuandoIdentificacionYaExiste() {
        // Arrange
        when(clienteRepository.existsByClienteId("CLI001")).thenReturn(false);
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(true);

        // Act & Assert
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> clienteService.createClient(clienteRequestDTO)
        );

        assertEquals("Ya existe un cliente con la identificación: 1234567890", exception.getMessage());
        verify(clienteRepository).existsByClienteId("CLI001");
        verify(clienteRepository).existsByIdentificacion("1234567890");
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void obtenerClientePorId_DeberiaRetornarCliente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        ClienteResponseDTO resultado = clienteService.getClientById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals("CLI001", resultado.getClienteId());

        verify(clienteRepository).findById(1L);
    }

    @Test
    void obtenerClientePorId_DeberiaLanzarExcepcionCuandoNoExiste() {
        // Arrange
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> clienteService.getClientById(999L)
        );

        assertEquals("Cliente no encontrado con ID: 999", exception.getMessage());
        verify(clienteRepository).findById(999L);
    }

    @Test
    void obtenerClientePorClienteId_DeberiaRetornarCliente() {
        // Arrange
        when(clienteRepository.findByClienteId("CLI001")).thenReturn(Optional.of(cliente));

        // Act
        ClienteResponseDTO resultado = clienteService.getClientByClienteId("CLI001");

        // Assert
        assertNotNull(resultado);
        assertEquals("CLI001", resultado.getClienteId());
        assertEquals("Juan Pérez", resultado.getNombre());

        verify(clienteRepository).findByClienteId("CLI001");
    }

    @Test
    void obtenerTodosLosClientes_DeberiaRetornarListaDeClientes() {
        // Arrange
        Cliente cliente2 = new Cliente("María García", "FEMENINO", 25, "0987654321", 
                                      "Avenida 456", "555-5678", "CLI002", "password456");
        cliente2.setId(2L);
        cliente2.setEstado(true);
        
        List<Cliente> clientes = Arrays.asList(cliente, cliente2);
        when(clienteRepository.findAll()).thenReturn(clientes);

        // Act
        List<ClienteResponseDTO> resultado = clienteService.getAllClients();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombre());
        assertEquals("María García", resultado.get(1).getNombre());

        verify(clienteRepository).findAll();
    }

    @Test
    void obtenerClientesPaginados_DeberiaRetornarPaginaDeClientes() {
        // Arrange
        List<Cliente> clientes = Arrays.asList(cliente);
        Page<Cliente> page = new PageImpl<>(clientes);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(clienteRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<ClienteResponseDTO> resultado = clienteService.getClientsPaginated(pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Juan Pérez", resultado.getContent().get(0).getNombre());

        verify(clienteRepository).findAll(pageable);
    }

    @Test
    void obtenerClientesActivos_DeberiaRetornarSoloClientesActivos() {
        // Arrange
        List<Cliente> clientesActivos = Arrays.asList(cliente);
        when(clienteRepository.findByEstado(true)).thenReturn(clientesActivos);

        // Act
        List<ClienteResponseDTO> resultado = clienteService.getActiveClients();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getEstado());

        verify(clienteRepository).findByEstado(true);
    }

    @Test
    void actualizarCliente_DeberiaActualizarClienteExitosamente() {
        // Arrange
        ClienteRequestDTO actualizacion = new ClienteRequestDTO();
        actualizacion.setNombre("Juan Pérez Actualizado");
        actualizacion.setGenero("MASCULINO");
        actualizacion.setEdad(31);
        actualizacion.setIdentificacion("1234567890");
        actualizacion.setDireccion("Nueva Calle 789");
        actualizacion.setTelefono("555-9999");
        actualizacion.setClienteId("CLI001");
        actualizacion.setContrasena("newpassword");
        actualizacion.setEstado(true);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByClienteId("CLI001")).thenReturn(false);
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        ClienteResponseDTO resultado = clienteService.updateClient(1L, actualizacion);

        // Assert
        assertNotNull(resultado);
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void eliminarCliente_DeberiaDesactivarCliente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(cuentaRepository.countByClienteId(1L)).thenReturn(0L);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        clienteService.deleteClient(1L);

        // Assert
        verify(clienteRepository).findById(1L);
        verify(cuentaRepository).countByClienteId(1L);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void eliminarCliente_DeberiaLanzarExcepcionCuandoTieneCuentas() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(cuentaRepository.countByClienteId(1L)).thenReturn(2L);

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> clienteService.deleteClient(1L)
        );

        assertEquals("No se puede eliminar el cliente: tiene 2 cuenta(s) asociada(s)", exception.getMessage());
        verify(clienteRepository).findById(1L);
        verify(cuentaRepository).countByClienteId(1L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void cambiarEstadoCliente_DeberiaCambiarEstado() {
        // Arrange
        cliente.setEstado(true);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        ClienteResponseDTO resultado = clienteService.changeClientStatus(1L);

        // Assert
        assertNotNull(resultado);
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void contarClientesActivos_DeberiaRetornarCantidadCorrecta() {
        // Arrange
        when(clienteRepository.countByEstado(true)).thenReturn(5L);

        // Act
        long resultado = clienteService.countActiveClients();

        // Assert
        assertEquals(5L, resultado);
        verify(clienteRepository).countByEstado(true);
    }

    @Test
    void existeClientePorClienteId_DeberiaRetornarTrue() {
        // Arrange
        when(clienteRepository.existsByClienteId("CLI001")).thenReturn(true);

        // Act
        boolean resultado = clienteService.existsClientByClienteId("CLI001");

        // Assert
        assertTrue(resultado);
        verify(clienteRepository).existsByClienteId("CLI001");
    }

    @Test
    void existeClientePorIdentificacion_DeberiaRetornarFalse() {
        // Arrange
        when(clienteRepository.existsByIdentificacion("9999999999")).thenReturn(false);

        // Act
        boolean resultado = clienteService.existsClientByIdentification("9999999999");

        // Assert
        assertFalse(resultado);
        verify(clienteRepository).existsByIdentificacion("9999999999");
    }
}
