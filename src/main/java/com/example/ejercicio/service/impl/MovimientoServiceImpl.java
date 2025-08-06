package com.example.ejercicio.service.impl;

import com.example.ejercicio.dto.MovimientoRequestDTO;
import com.example.ejercicio.dto.MovimientoResponseDTO;
import com.example.ejercicio.exception.ResourceNotFoundException;
import com.example.ejercicio.exception.SaldoInsuficienteException;
import com.example.ejercicio.model.Cuenta;
import com.example.ejercicio.model.Movimiento;
import com.example.ejercicio.repository.CuentaRepository;
import com.example.ejercicio.repository.MovimientoRepository;
import com.example.ejercicio.service.MovimientoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoServiceImpl implements MovimientoService {
    
    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Cuenta no encontrada con ID: ";
    private static final String MOVEMENT_NOT_FOUND_MESSAGE = "Movimiento no encontrado con ID: ";
    private static final String INACTIVE_ACCOUNT_MESSAGE = "La cuenta está inactiva";
    private static final String INSUFFICIENT_BALANCE_MESSAGE = "Saldo insuficiente para realizar el débito";
    
    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    
    public MovimientoServiceImpl(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }
    
    @Override
    public MovimientoResponseDTO createMovement(MovimientoRequestDTO movimientoRequestDTO) {
        // Verify account exists
        Cuenta cuenta = cuentaRepository.findById(movimientoRequestDTO.getCuentaId())
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE + movimientoRequestDTO.getCuentaId()));
        
        // Verify account is active
        if (!cuenta.getEstado()) {
            throw new IllegalArgumentException(INACTIVE_ACCOUNT_MESSAGE);
        }
        
        BigDecimal valor = movimientoRequestDTO.getValor();
        BigDecimal saldoActual = cuenta.getSaldoActual();
        
        // Verify sufficient balance for debits (negative values)
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal nuevoSaldo = saldoActual.add(valor);
            if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                throw new SaldoInsuficienteException("Saldo insuficiente. Saldo actual: " + saldoActual);
            }
        }
        
        // Create new movement
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento(movimientoRequestDTO.getTipoMovimiento());
        movimiento.setValor(valor);
        movimiento.setSaldo(saldoActual.add(valor));
        movimiento.setDescripcion(movimientoRequestDTO.getDescripcion());
        movimiento.setCuenta(cuenta);
        
        // Save movement first
        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);
        
        // Update account balance
        cuenta.updateBalance(valor);
        cuentaRepository.save(cuenta);
        
        return convertToResponseDTO(movimientoGuardado);
    }
    
    @Override
    public MovimientoResponseDTO getMovementById(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MOVEMENT_NOT_FOUND_MESSAGE + id));
        return convertToResponseDTO(movimiento);
    }
    
    @Override
    public List<MovimientoResponseDTO> getAllMovements() {
        return movimientoRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<MovimientoResponseDTO> getMovementsPaginated(Pageable pageable) {
        return movimientoRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }
    
    @Override
    public List<MovimientoResponseDTO> getMovementsByAccount(Long cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<MovimientoResponseDTO> getMovementsByAccountPaginated(Long cuentaId, Pageable pageable) {
        return movimientoRepository.findByCuentaId(cuentaId, pageable)
                .map(this::convertToResponseDTO);
    }
    
    @Override
    public List<MovimientoResponseDTO> getMovementsByDateRange(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoRepository.findByFechaBetween(fechaInicio, fechaFin).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<MovimientoResponseDTO> getMovementsByAccountAndDateRange(Long cuentaId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoRepository.findByCuentaIdAndFechaBetween(cuentaId, fechaInicio, fechaFin).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<MovimientoResponseDTO> getMovementsByType(String tipoMovimiento) {
        return movimientoRepository.findByTipoMovimiento(tipoMovimiento).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public MovimientoResponseDTO updateMovement(Long id, MovimientoRequestDTO movimientoRequestDTO) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MOVEMENT_NOT_FOUND_MESSAGE + id));
        
        // Only allow updating description to maintain integrity
        movimiento.setDescripcion(movimientoRequestDTO.getDescripcion());
        Movimiento movimientoActualizado = movimientoRepository.save(movimiento);
        return convertToResponseDTO(movimientoActualizado);
    }
    
    @Override
    public void deleteMovement(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MOVEMENT_NOT_FOUND_MESSAGE + id));
        Cuenta cuenta = movimiento.getCuenta();
        
        // Reverse the balance effect
        cuenta.updateBalance(movimiento.getValor().negate());
        cuentaRepository.save(cuenta);
        
        // Delete the movement
        movimientoRepository.delete(movimiento);
    }
    
    @Override
    public List<MovimientoResponseDTO> searchMovements(String busqueda) {
        return movimientoRepository.buscarMovimientos(busqueda).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public MovimientoResponseDTO getLastMovementByAccount(Long cuentaId) {
        List<Movimiento> movimientos = movimientoRepository.findByCuentaIdOrderByFechaDesc(cuentaId);
        if (movimientos.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron movimientos para la cuenta con ID: " + cuentaId);
        }
        return convertToResponseDTO(movimientos.get(0));
    }
    
    @Override
    public List<MovimientoResponseDTO> getDebitMovementsByAccount(Long cuentaId) {
        return movimientoRepository.findMovimientosDebitoPorCuenta(cuentaId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<MovimientoResponseDTO> getCreditMovementsByAccount(Long cuentaId) {
        return movimientoRepository.findMovimientosCreditoPorCuenta(cuentaId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public long countMovementsByAccount(Long cuentaId) {
        return movimientoRepository.countByCuentaId(cuentaId);
    }
    
    /**
     * Converts a Movimiento entity to a MovimientoResponseDTO
     */
    private MovimientoResponseDTO convertToResponseDTO(Movimiento movimiento) {
        MovimientoResponseDTO responseDTO = new MovimientoResponseDTO();
        responseDTO.setId(movimiento.getId());
        responseDTO.setFecha(movimiento.getFecha());
        responseDTO.setTipoMovimiento(movimiento.getTipoMovimiento());
        responseDTO.setValor(movimiento.getValor());
        responseDTO.setSaldo(movimiento.getSaldo());
        responseDTO.setDescripcion(movimiento.getDescripcion());
        responseDTO.setFechaCreacion(movimiento.getFechaCreacion());
        
        // Add account information
        if (movimiento.getCuenta() != null) {
            responseDTO.setCuentaId(movimiento.getCuenta().getId());
            responseDTO.setNumeroCuenta(movimiento.getCuenta().getNumeroCuenta());
            responseDTO.setClienteNombre(movimiento.getCuenta().getCliente().getNombre());
        }
        
        return responseDTO;
    }
}
