package com.example.ejercicio.service;

import com.example.ejercicio.dto.CuentaRequestDTO;
import com.example.ejercicio.dto.CuentaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {
    
    /**
     * Creates a new account
     */
    CuentaResponseDTO createAccount(CuentaRequestDTO cuentaRequestDTO);
    
    /**
     * Gets an account by ID
     */
    CuentaResponseDTO getAccountById(Long id);
    
    /**
     * Gets an account by account number
     */
    CuentaResponseDTO getAccountByNumber(String numeroCuenta);
    
    /**
     * Gets all accounts
     */
    List<CuentaResponseDTO> getAllAccounts();
    
    /**
     * Gets accounts with pagination
     */
    Page<CuentaResponseDTO> getAccountsPaginated(Pageable pageable);
    
    /**
     * Gets active accounts
     */
    List<CuentaResponseDTO> getActiveAccounts();
    
    /**
     * Gets accounts by client
     */
    List<CuentaResponseDTO> getAccountsByClient(Long clienteId);
    
    /**
     * Gets active accounts by client
     */
    List<CuentaResponseDTO> getActiveAccountsByClient(Long clienteId);
    
    /**
     * Updates an account
     */
    CuentaResponseDTO updateAccount(Long id, CuentaRequestDTO cuentaRequestDTO);
    
    /**
     * Deletes an account (soft delete)
     */
    void deleteAccount(Long id);
    
    /**
     * Activates an account
     */
    CuentaResponseDTO activateAccount(Long id);
    
    /**
     * Searches accounts by search term
     */
    List<CuentaResponseDTO> searchAccounts(String busqueda);
    
    /**
     * Checks if an account exists with the given number
     */
    boolean existsAccountByNumber(String numeroCuenta);
    
    /**
     * Gets total balance for a client
     */
    BigDecimal getTotalBalanceByClient(Long clienteId);
    
    /**
     * Counts active accounts
     */
    long countActiveAccounts();
    
    /**
     * Gets accounts by type
     */
    List<CuentaResponseDTO> getAccountsByType(String tipoCuenta);
    
    /**
     * Gets accounts with minimum balance
     */
    List<CuentaResponseDTO> getAccountsWithMinimumBalance(BigDecimal saldoMinimo);
    
    /**
     * Changes account status
     */
    CuentaResponseDTO changeAccountStatus(Long id);
    
    /**
     * Gets account balance
     */
    BigDecimal getAccountBalance(Long id);
    
    /**
     * Counts movements by account
     */
    long countMovementsByAccount(Long id);
}
