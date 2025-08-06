package com.example.ejercicio.service;

import com.example.ejercicio.dto.MovimientoRequestDTO;
import com.example.ejercicio.dto.MovimientoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoService {
    
    /**
     * Creates a new movement
     */
    MovimientoResponseDTO createMovement(MovimientoRequestDTO movimientoRequestDTO);
    
    /**
     * Gets a movement by ID
     */
    MovimientoResponseDTO getMovementById(Long id);
    
    /**
     * Gets all movements
     */
    List<MovimientoResponseDTO> getAllMovements();
    
    /**
     * Gets movements with pagination
     */
    Page<MovimientoResponseDTO> getMovementsPaginated(Pageable pageable);
    
    /**
     * Gets movements by account
     */
    List<MovimientoResponseDTO> getMovementsByAccount(Long cuentaId);
    
    /**
     * Gets movements by account with pagination
     */
    Page<MovimientoResponseDTO> getMovementsByAccountPaginated(Long cuentaId, Pageable pageable);
    
    /**
     * Gets movements by date range
     */
    List<MovimientoResponseDTO> getMovementsByDateRange(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Gets movements by account and date range
     */
    List<MovimientoResponseDTO> getMovementsByAccountAndDateRange(Long cuentaId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Gets movements by type
     */
    List<MovimientoResponseDTO> getMovementsByType(String tipoMovimiento);
    
    /**
     * Updates a movement
     */
    MovimientoResponseDTO updateMovement(Long id, MovimientoRequestDTO movimientoRequestDTO);
    
    /**
     * Deletes a movement
     */
    void deleteMovement(Long id);
    
    /**
     * Searches movements by search term
     */
    List<MovimientoResponseDTO> searchMovements(String busqueda);
    
    /**
     * Gets the last movement of an account
     */
    MovimientoResponseDTO getLastMovementByAccount(Long cuentaId);
    
    /**
     * Gets debit movements by account
     */
    List<MovimientoResponseDTO> getDebitMovementsByAccount(Long cuentaId);
    
    /**
     * Gets credit movements by account
     */
    List<MovimientoResponseDTO> getCreditMovementsByAccount(Long cuentaId);
    
    /**
     * Counts movements by account
     */
    long countMovementsByAccount(Long cuentaId);
}
