package com.example.ejercicio.controller;

import com.example.ejercicio.dto.MovimientoRequestDTO;
import com.example.ejercicio.dto.MovimientoResponseDTO;
import com.example.ejercicio.service.MovimientoService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
@Tag(name = "Movimientos", description = "API para gestión de movimientos bancarios")
@CrossOrigin(origins = "*")
public class MovimientoController {
    
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String MOVEMENT_NOT_FOUND_MESSAGE = "Movimiento no encontrado";
    private static final String MOVEMENT_CREATED_MESSAGE = "Movimiento creado exitosamente";
    private static final String MOVEMENT_UPDATED_MESSAGE = "Movimiento actualizado exitosamente";
    private static final String MOVEMENT_DELETED_MESSAGE = "Movimiento eliminado exitosamente";
    private static final String MOVEMENT_FOUND_MESSAGE = "Movimiento encontrado";
    private static final String MOVEMENTS_FOUND_MESSAGE = "Movimientos encontrados";
    private static final String LAST_MOVEMENT_FOUND_MESSAGE = "Último movimiento encontrado";
    private static final String DEBIT_MOVEMENTS_FOUND_MESSAGE = "Movimientos de débito encontrados";
    private static final String CREDIT_MOVEMENTS_FOUND_MESSAGE = "Movimientos de crédito encontrados";
    private static final String MOVEMENTS_COUNT_OBTAINED_MESSAGE = "Número de movimientos obtenido";
    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Cuenta no encontrada";
    private static final String ACCOUNT_NOT_FOUND_OR_NO_MOVEMENTS_MESSAGE = "Cuenta no encontrada o sin movimientos";
    private static final String INVALID_DATA_OR_INSUFFICIENT_BALANCE_MESSAGE = "Datos de entrada inválidos o saldo insuficiente";
    private static final String INVALID_DATA_MESSAGE = "Datos de entrada inválidos";
    
    private final MovimientoService movimientoService;
    
    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }
    
    @PostMapping
    @Operation(summary = "Crear un nuevo movimiento", description = "Crea un nuevo movimiento bancario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = MOVEMENT_CREATED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = MovimientoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = INVALID_DATA_OR_INSUFFICIENT_BALANCE_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<MovimientoResponseDTO> createMovement(@Valid @RequestBody MovimientoRequestDTO movimientoRequestDTO) {
        MovimientoResponseDTO movimientoResponse = movimientoService.createMovement(movimientoRequestDTO);
        return new ResponseEntity<>(movimientoResponse, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener movimiento por ID", description = "Obtiene los detalles de un movimiento específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MOVEMENT_FOUND_MESSAGE,
                    content = @Content(schema = @Schema(implementation = MovimientoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = MOVEMENT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<MovimientoResponseDTO> getMovementById(@Parameter(description = "ID del movimiento") @PathVariable Long id) {
        MovimientoResponseDTO movimiento = movimientoService.getMovementById(id);
        return ResponseEntity.ok(movimiento);
    }
    
    @GetMapping
    @Operation(summary = "Obtener todos los movimientos", description = "Obtiene la lista de todos los movimientos")
    public ResponseEntity<List<MovimientoResponseDTO>> getAllMovements() {
        List<MovimientoResponseDTO> movimientos = movimientoService.getAllMovements();
        return ResponseEntity.ok(movimientos);
    }
    
    @GetMapping("/paginados")
    @Operation(summary = "Obtener movimientos paginados", description = "Obtiene la lista de movimientos con paginación")
    public ResponseEntity<Page<MovimientoResponseDTO>> getMovementsPaginated(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<MovimientoResponseDTO> movimientos = movimientoService.getMovementsPaginated(pageable);
        return ResponseEntity.ok(movimientos);
    }
    
    @GetMapping("/cuenta/{cuentaId}")
    @Operation(summary = "Obtener movimientos por cuenta", description = "Obtiene todos los movimientos de una cuenta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MOVEMENTS_FOUND_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<List<MovimientoResponseDTO>> getMovementsByAccount(@Parameter(description = "ID de la cuenta") @PathVariable Long cuentaId) {
        List<MovimientoResponseDTO> movimientos = movimientoService.getMovementsByAccount(cuentaId);
        return ResponseEntity.ok(movimientos);
    }
    
    @GetMapping("/cuenta/{cuentaId}/paginados")
    @Operation(summary = "Obtener movimientos por cuenta paginados", description = "Obtiene los movimientos de una cuenta con paginación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MOVEMENTS_FOUND_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<Page<MovimientoResponseDTO>> getMovementsByAccountPaginated(@Parameter(description = "ID de la cuenta") @PathVariable Long cuentaId, @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<MovimientoResponseDTO> movimientos = movimientoService.getMovementsByAccountPaginated(cuentaId, pageable);
        return ResponseEntity.ok(movimientos);
    }
    
    @GetMapping("/fecha-rango")
    @Operation(summary = "Obtener movimientos por rango de fechas", description = "Obtiene movimientos entre dos fechas específicas")
    public ResponseEntity<List<MovimientoResponseDTO>> getMovementsByDateRange(@Parameter(description = "Fecha de inicio") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio, @Parameter(description = "Fecha de fin") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<MovimientoResponseDTO> movimientos = movimientoService.getMovementsByDateRange(fechaInicio, fechaFin);
        return ResponseEntity.ok(movimientos);
    }
    
    @GetMapping("/cuenta/{cuentaId}/fecha-rango")
    @Operation(summary = "Obtener movimientos por cuenta y rango de fechas", description = "Obtiene movimientos de una cuenta entre dos fechas específicas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MOVEMENTS_FOUND_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<List<MovimientoResponseDTO>> getMovementsByAccountAndDateRange(@Parameter(description = "ID de la cuenta") @PathVariable Long cuentaId, @Parameter(description = "Fecha de inicio") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio, @Parameter(description = "Fecha de fin") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<MovimientoResponseDTO> movimientos = movimientoService.getMovementsByAccountAndDateRange(cuentaId, fechaInicio, fechaFin);
        return ResponseEntity.ok(movimientos);
    }
    
    @GetMapping("/tipo/{tipoMovimiento}")
    @Operation(summary = "Obtener movimientos por tipo", description = "Obtiene todos los movimientos de un tipo específico")
    public ResponseEntity<List<MovimientoResponseDTO>> getMovementsByType(
            @Parameter(description = "Tipo de movimiento") @PathVariable String tipoMovimiento) {
        List<MovimientoResponseDTO> movimientos = movimientoService.getMovementsByType(tipoMovimiento);
        return ResponseEntity.ok(movimientos);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un movimiento", description = "Actualiza los datos de un movimiento existente (solo descripción)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MOVEMENT_UPDATED_MESSAGE),
            @ApiResponse(responseCode = "404", description = MOVEMENT_NOT_FOUND_MESSAGE),
            @ApiResponse(responseCode = "400", description = INVALID_DATA_MESSAGE)
    })
    public ResponseEntity<MovimientoResponseDTO> updateMovement(@Parameter(description = "ID del movimiento") @PathVariable Long id, @Valid @RequestBody MovimientoRequestDTO movimientoRequestDTO) {
        MovimientoResponseDTO movimientoActualizado = movimientoService.updateMovement(id, movimientoRequestDTO);
        return ResponseEntity.ok(movimientoActualizado);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un movimiento", description = "Elimina un movimiento y revierte su efecto en el saldo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = MOVEMENT_DELETED_MESSAGE),
            @ApiResponse(responseCode = "404", description = MOVEMENT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<Void> deleteMovement(@Parameter(description = "ID del movimiento") @PathVariable Long id) {
        movimientoService.deleteMovement(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar movimientos", description = "Busca movimientos por descripción, tipo o información de cuenta")
    public ResponseEntity<List<MovimientoResponseDTO>> searchMovements(@Parameter(description = "Término de búsqueda") @RequestParam String busqueda) {
        List<MovimientoResponseDTO> movimientos = movimientoService.searchMovements(busqueda);
        return ResponseEntity.ok(movimientos);
    }
    
    @GetMapping("/cuenta/{cuentaId}/ultimo")
    @Operation(summary = "Obtener último movimiento por cuenta", description = "Obtiene el movimiento más reciente de una cuenta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = LAST_MOVEMENT_FOUND_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_OR_NO_MOVEMENTS_MESSAGE)
    })
    public ResponseEntity<MovimientoResponseDTO> getLastMovementByAccount(@Parameter(description = "ID de la cuenta") @PathVariable Long cuentaId) {
        MovimientoResponseDTO movimiento = movimientoService.getLastMovementByAccount(cuentaId);
        return ResponseEntity.ok(movimiento);
    }
    
    @GetMapping("/cuenta/{cuentaId}/debitos")
    @Operation(summary = "Obtener movimientos de débito por cuenta", description = "Obtiene todos los movimientos de débito (negativos) de una cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = DEBIT_MOVEMENTS_FOUND_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<List<MovimientoResponseDTO>> getDebitMovementsByAccount(@Parameter(description = "ID de la cuenta") @PathVariable Long cuentaId) {
        List<MovimientoResponseDTO> movimientos = movimientoService.getDebitMovementsByAccount(cuentaId);
        return ResponseEntity.ok(movimientos);
    }
    
    @GetMapping("/cuenta/{cuentaId}/creditos")
    @Operation(summary = "Obtener movimientos de crédito por cuenta", description = "Obtiene todos los movimientos de crédito (positivos) de una cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = CREDIT_MOVEMENTS_FOUND_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<List<MovimientoResponseDTO>> getCreditMovementsByAccount(@Parameter(description = "ID de la cuenta") @PathVariable Long cuentaId) {
        List<MovimientoResponseDTO> movimientos = movimientoService.getCreditMovementsByAccount(cuentaId);
        return ResponseEntity.ok(movimientos);
    }
    
    @GetMapping("/cuenta/{cuentaId}/contar")
    @Operation(summary = "Contar movimientos por cuenta", description = "Obtiene el número total de movimientos de una cuenta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MOVEMENTS_COUNT_OBTAINED_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<Long> countMovementsByAccount(@Parameter(description = "ID de la cuenta") @PathVariable Long cuentaId) {
        long count = movimientoService.countMovementsByAccount(cuentaId);
        return ResponseEntity.ok(count);
    }
}
