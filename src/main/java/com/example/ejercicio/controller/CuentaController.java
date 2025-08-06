package com.example.ejercicio.controller;

import com.example.ejercicio.dto.CuentaRequestDTO;
import com.example.ejercicio.dto.CuentaResponseDTO;
import com.example.ejercicio.service.CuentaService;
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

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cuentas")
@Tag(name = "Cuentas", description = "API para gestión de cuentas bancarias")
@CrossOrigin(origins = "*")
public class CuentaController {
    
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Cuenta no encontrada";
    private static final String ACCOUNT_CREATED_MESSAGE = "Cuenta creada exitosamente";
    private static final String ACCOUNT_UPDATED_MESSAGE = "Cuenta actualizada exitosamente";
    private static final String ACCOUNT_DELETED_MESSAGE = "Cuenta eliminada exitosamente";
    private static final String ACCOUNT_ACTIVATED_MESSAGE = "Cuenta activada exitosamente";
    private static final String ACCOUNT_STATUS_CHANGED_MESSAGE = "Estado cambiado exitosamente";
    private static final String ACCOUNT_FOUND_MESSAGE = "Cuenta encontrada";
    private static final String ACCOUNTS_FOUND_MESSAGE = "Cuentas encontradas";
    private static final String BALANCE_OBTAINED_MESSAGE = "Saldo obtenido";
    private static final String MOVEMENTS_COUNT_OBTAINED_MESSAGE = "Número de movimientos obtenido";
    private static final String TOTAL_BALANCE_CALCULATED_MESSAGE = "Saldo total calculado";
    private static final String CLIENT_NOT_FOUND_MESSAGE = "Cliente no encontrado";
    private static final String INVALID_DATA_MESSAGE = "Datos de entrada inválidos";
    private static final String ACCOUNT_ALREADY_EXISTS_MESSAGE = "Cuenta ya existe con ese número";
    
    private final CuentaService cuentaService;
    
    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }
    
    @PostMapping
    @Operation(summary = "Crear una nueva cuenta", description = "Crea una nueva cuenta bancaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = ACCOUNT_CREATED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = CuentaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = INVALID_DATA_MESSAGE),
            @ApiResponse(responseCode = "409", description = ACCOUNT_ALREADY_EXISTS_MESSAGE)
    })
    public ResponseEntity<CuentaResponseDTO> createAccount(@Valid @RequestBody CuentaRequestDTO cuentaRequestDTO) {
        CuentaResponseDTO cuentaResponse = cuentaService.createAccount(cuentaRequestDTO);
        return new ResponseEntity<>(cuentaResponse, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener cuenta por ID", description = "Obtiene los detalles de una cuenta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ACCOUNT_FOUND_MESSAGE,
                    content = @Content(schema = @Schema(implementation = CuentaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<CuentaResponseDTO> getAccountById(@Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        CuentaResponseDTO cuenta = cuentaService.getAccountById(id);
        return ResponseEntity.ok(cuenta);
    }
    
    @GetMapping("/numero/{numeroCuenta}")
    @Operation(summary = "Obtener cuenta por número", description = "Obtiene una cuenta por su número único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ACCOUNT_FOUND_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<CuentaResponseDTO> getAccountByNumber(@Parameter(description = "Número de cuenta") @PathVariable String numeroCuenta) {
        CuentaResponseDTO cuenta = cuentaService.getAccountByNumber(numeroCuenta);
        return ResponseEntity.ok(cuenta);
    }
    
    @GetMapping
    @Operation(summary = "Obtener todas las cuentas", description = "Obtiene la lista de todas las cuentas")
    public ResponseEntity<List<CuentaResponseDTO>> getAllAccounts() {
        List<CuentaResponseDTO> cuentas = cuentaService.getAllAccounts();
        return ResponseEntity.ok(cuentas);
    }
    
    @GetMapping("/paginadas")
    @Operation(summary = "Obtener cuentas paginadas", description = "Obtiene la lista de cuentas con paginación")
    public ResponseEntity<Page<CuentaResponseDTO>> getAccountsPaginated(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<CuentaResponseDTO> cuentas = cuentaService.getAccountsPaginated(pageable);
        return ResponseEntity.ok(cuentas);
    }
    
    @GetMapping("/activas")
    @Operation(summary = "Obtener cuentas activas", description = "Obtiene la lista de cuentas con estado activo")
    public ResponseEntity<List<CuentaResponseDTO>> getActiveAccounts() {
        List<CuentaResponseDTO> cuentas = cuentaService.getActiveAccounts();
        return ResponseEntity.ok(cuentas);
    }
    
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener cuentas por cliente", description = "Obtiene todas las cuentas de un cliente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ACCOUNTS_FOUND_MESSAGE),
            @ApiResponse(responseCode = "404", description = CLIENT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<List<CuentaResponseDTO>> getAccountsByClient(@Parameter(description = "ID del cliente") @PathVariable Long clienteId) {
        List<CuentaResponseDTO> cuentas = cuentaService.getAccountsByClient(clienteId);
        return ResponseEntity.ok(cuentas);
    }
    
    @GetMapping("/cliente/{clienteId}/activas")
    @Operation(summary = "Obtener cuentas activas por cliente", description = "Obtiene las cuentas activas de un cliente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ACCOUNTS_FOUND_MESSAGE),
            @ApiResponse(responseCode = "404", description = CLIENT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<List<CuentaResponseDTO>> getActiveAccountsByClient(@Parameter(description = "ID del cliente") @PathVariable Long clienteId) {
        List<CuentaResponseDTO> cuentas = cuentaService.getActiveAccountsByClient(clienteId);
        return ResponseEntity.ok(cuentas);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una cuenta", description = "Actualiza los datos de una cuenta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ACCOUNT_UPDATED_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE),
            @ApiResponse(responseCode = "400", description = INVALID_DATA_MESSAGE)
    })
    public ResponseEntity<CuentaResponseDTO> updateAccount(@Parameter(description = "ID de la cuenta") @PathVariable Long id, @Valid @RequestBody CuentaRequestDTO cuentaRequestDTO) {
        CuentaResponseDTO cuentaActualizada = cuentaService.updateAccount(id, cuentaRequestDTO);
        return ResponseEntity.ok(cuentaActualizada);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una cuenta", description = "Realiza eliminación lógica (soft delete) de una cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = ACCOUNT_DELETED_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<Void> deleteAccount(@Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        cuentaService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar una cuenta", description = "Cambia el estado de la cuenta a activo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ACCOUNT_ACTIVATED_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<CuentaResponseDTO> activateAccount(@Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        CuentaResponseDTO cuentaActivada = cuentaService.activateAccount(id);
        return ResponseEntity.ok(cuentaActivada);
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar cuentas", description = "Busca cuentas por número, tipo o información del cliente")
    public ResponseEntity<List<CuentaResponseDTO>> searchAccounts(@Parameter(description = "Término de búsqueda") @RequestParam String busqueda) {
        List<CuentaResponseDTO> cuentas = cuentaService.searchAccounts(busqueda);
        return ResponseEntity.ok(cuentas);
    }
    
    @GetMapping("/existe/numero/{numeroCuenta}")
    @Operation(summary = "Verificar existencia por número", description = "Verifica si existe una cuenta con el número dado")
    public ResponseEntity<Boolean> existsAccountByNumber(@Parameter(description = "Número de cuenta a verificar") @PathVariable String numeroCuenta) {
        boolean existe = cuentaService.existsAccountByNumber(numeroCuenta);
        return ResponseEntity.ok(existe);
    }
    
    @GetMapping("/cliente/{clienteId}/saldo-total")
    @Operation(summary = "Obtener saldo total por cliente", description = "Obtiene la suma de saldos de todas las cuentas activas de un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = TOTAL_BALANCE_CALCULATED_MESSAGE),
            @ApiResponse(responseCode = "404", description = CLIENT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<BigDecimal> getTotalBalanceByClient(@Parameter(description = "ID del cliente") @PathVariable Long clienteId) {
        BigDecimal saldoTotal = cuentaService.getTotalBalanceByClient(clienteId);
        return ResponseEntity.ok(saldoTotal);
    }
    
    @GetMapping("/contar/activas")
    @Operation(summary = "Contar cuentas activas", description = "Obtiene el número total de cuentas activas")
    public ResponseEntity<Long> countActiveAccounts() {
        long count = cuentaService.countActiveAccounts();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/tipo/{tipoCuenta}")
    @Operation(summary = "Obtener cuentas por tipo", description = "Obtiene todas las cuentas de un tipo específico")
    public ResponseEntity<List<CuentaResponseDTO>> getAccountsByType(@Parameter(description = "Tipo de cuenta") @PathVariable String tipoCuenta) {
        List<CuentaResponseDTO> cuentas = cuentaService.getAccountsByType(tipoCuenta);
        return ResponseEntity.ok(cuentas);
    }
    
    @GetMapping("/saldo-minimo/{saldoMinimo}")
    @Operation(summary = "Obtener cuentas con saldo mínimo", description = "Obtiene cuentas que tienen al menos el saldo especificado")
    public ResponseEntity<List<CuentaResponseDTO>> getAccountsWithMinimumBalance(@Parameter(description = "Saldo mínimo") @PathVariable BigDecimal saldoMinimo) {
        List<CuentaResponseDTO> cuentas = cuentaService.getAccountsWithMinimumBalance(saldoMinimo);
        return ResponseEntity.ok(cuentas);
    }
    
    @PatchMapping("/{id}/cambiar-estado")
    @Operation(summary = "Cambiar estado de cuenta", description = "Alterna el estado de la cuenta (activo/inactivo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ACCOUNT_STATUS_CHANGED_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<CuentaResponseDTO> changeAccountStatus(@Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        CuentaResponseDTO cuentaActualizada = cuentaService.changeAccountStatus(id);
        return ResponseEntity.ok(cuentaActualizada);
    }
    
    @GetMapping("/{id}/saldo")
    @Operation(summary = "Obtener saldo de cuenta", description = "Obtiene el saldo actual de una cuenta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = BALANCE_OBTAINED_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<BigDecimal> getAccountBalance(@Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        BigDecimal saldo = cuentaService.getAccountBalance(id);
        return ResponseEntity.ok(saldo);
    }
    
    @GetMapping("/{id}/contar-movimientos")
    @Operation(summary = "Contar movimientos por cuenta", description = "Obtiene el número de movimientos de una cuenta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MOVEMENTS_COUNT_OBTAINED_MESSAGE),
            @ApiResponse(responseCode = "404", description = ACCOUNT_NOT_FOUND_MESSAGE)
    })
    public ResponseEntity<Long> countMovementsByAccount(@Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        long count = cuentaService.countMovementsByAccount(id);
        return ResponseEntity.ok(count);
    }
}
