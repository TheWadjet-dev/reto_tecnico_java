package com.example.ejercicio.controller;

import com.example.ejercicio.dto.ClienteRequestDTO;
import com.example.ejercicio.dto.ClienteResponseDTO;
import com.example.ejercicio.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "API para gestión de clientes")
@CrossOrigin(origins = "*")
public class ClienteController {
    
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String CLIENT_NOT_FOUND_MESSAGE = "Cliente no encontrado";
    private static final String CLIENT_CREATED_MESSAGE = "Cliente creado exitosamente";
    private static final String CLIENT_UPDATED_MESSAGE = "Cliente actualizado exitosamente";
    private static final String CLIENT_DELETED_MESSAGE = "Cliente eliminado exitosamente";
    private static final String CLIENT_ACTIVATED_MESSAGE = "Cliente activado exitosamente";
    private static final String INVALID_DATA_MESSAGE = "Datos de entrada inválidos";
    private static final String CLIENT_EXISTS_MESSAGE = "Cliente ya existe con esa identificación o clienteId";
    
    private final ClienteService clienteService;
    
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    
    @PostMapping
    @Operation(summary = "Crear un nuevo cliente", description = "Crea un nuevo cliente en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = CLIENT_CREATED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = INVALID_DATA_MESSAGE),
            @ApiResponse(responseCode = "409", description = CLIENT_EXISTS_MESSAGE)
    })
    public ResponseEntity<ClienteResponseDTO> createClient(@Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO clienteResponse = clienteService.createClient(clienteRequestDTO);
        return new ResponseEntity<>(clienteResponse, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID", description = "Obtiene los detalles de un cliente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = CLIENT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<ClienteResponseDTO> getClientById(@Parameter(description = "ID del cliente") @PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.getClientById(id);
        return ResponseEntity.ok(cliente);
    }
    
    @GetMapping
    @Operation(summary = "Obtener todos los clientes", description = "Obtiene la lista de todos los clientes")
    public ResponseEntity<List<ClienteResponseDTO>> getAllClients() {
        List<ClienteResponseDTO> clientes = clienteService.getAllClients();
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/paginados")
    @Operation(summary = "Obtener clientes paginados", description = "Obtiene la lista de clientes con paginación")
    public ResponseEntity<Page<ClienteResponseDTO>> getClientsPaginated(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<ClienteResponseDTO> clientes = clienteService.getClientsPaginated(pageable);
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/cliente-id/{clienteId}")
    @Operation(summary = "Obtener cliente por clienteId", description = "Obtiene un cliente por su clienteId único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = CLIENT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<ClienteResponseDTO> getClientByClienteId(@Parameter(description = "ClienteId único") @PathVariable String clienteId) {
        ClienteResponseDTO cliente = clienteService.getClientByClienteId(clienteId);
        return ResponseEntity.ok(cliente);
    }
    
    @GetMapping("/identificacion/{identificacion}")
    @Operation(summary = "Obtener cliente por identificación", description = "Obtiene un cliente por su número de identificación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = CLIENT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<ClienteResponseDTO> getClientByIdentification(@Parameter(description = "Número de identificación") @PathVariable String identificacion) {
        ClienteResponseDTO cliente = clienteService.getClientByIdentification(identificacion);
        return ResponseEntity.ok(cliente);
    }
    
    @GetMapping("/activos")
    @Operation(summary = "Obtener clientes activos", description = "Obtiene la lista de clientes con estado activo")
    public ResponseEntity<List<ClienteResponseDTO>> getActiveClients() {
        List<ClienteResponseDTO> clientes = clienteService.getActiveClients();
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar clientes", description = "Busca clientes por nombre, identificación o clienteId")
    public ResponseEntity<List<ClienteResponseDTO>> searchClients(@Parameter(description = "Término de búsqueda") @RequestParam String busqueda) {
        List<ClienteResponseDTO> clientes = clienteService.searchClients(busqueda);
        return ResponseEntity.ok(clientes);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente", description = "Actualiza los datos de un cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = CLIENT_UPDATED_MESSAGE),
            @ApiResponse(responseCode = "404", description = CLIENT_NOT_FOUND_MESSAGE),
            @ApiResponse(responseCode = "400", description = INVALID_DATA_MESSAGE)
    })
    public ResponseEntity<ClienteResponseDTO> updateClient(@Parameter(description = "ID del cliente") @PathVariable Long id, @Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO clienteActualizado = clienteService.updateClient(id, clienteRequestDTO);
        return ResponseEntity.ok(clienteActualizado);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente", description = "Realiza eliminación lógica (soft delete) de un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = CLIENT_DELETED_MESSAGE),
            @ApiResponse(responseCode = "404", description = CLIENT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<Void> deleteClient(@Parameter(description = "ID del cliente") @PathVariable Long id) {
        clienteService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar un cliente", description = "Cambia el estado del cliente a activo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = CLIENT_ACTIVATED_MESSAGE),
            @ApiResponse(responseCode = "404", description = CLIENT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<ClienteResponseDTO> activateClient(@Parameter(description = "ID del cliente") @PathVariable Long id) {
        ClienteResponseDTO clienteActivado = clienteService.activateClient(id);
        return ResponseEntity.ok(clienteActivado);
    }
    
    @GetMapping("/existe/cliente-id/{clienteId}")
    @Operation(summary = "Verificar existencia por clienteId", description = "Verifica si existe un cliente con el clienteId dado")
    public ResponseEntity<Boolean> existsByClienteId(@Parameter(description = "ClienteId a verificar") @PathVariable String clienteId) {
        boolean existe = clienteService.existsClientByClienteId(clienteId);
        return ResponseEntity.ok(existe);
    }
    
    @GetMapping("/existe/identificacion/{identificacion}")
    @Operation(summary = "Verificar existencia por identificación", description = "Verifica si existe un cliente con la identificación dada")
    public ResponseEntity<Boolean> existsByIdentification(@Parameter(description = "Identificación a verificar") @PathVariable String identificacion) {
        boolean existe = clienteService.existsClientByIdentification(identificacion);
        return ResponseEntity.ok(existe);
    }
    
    @GetMapping("/contar/activos")
    @Operation(summary = "Contar clientes activos", description = "Obtiene el número total de clientes activos")
    public ResponseEntity<Long> countActiveClients() {
        long count = clienteService.countActiveClients();
        return ResponseEntity.ok(count);
    }
}
