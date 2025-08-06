package com.example.ejercicio.repository;

import com.example.ejercicio.model.Cuenta;
import com.example.ejercicio.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    
    /**
     * Busca una cuenta por su número
     */
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    
    /**
     * Verifica si existe una cuenta con el número dado
     */
    boolean existsByNumeroCuenta(String numeroCuenta);
    
    /**
     * Obtiene cuentas por cliente
     */
    List<Cuenta> findByCliente(Cliente cliente);
    
    /**
     * Obtiene cuentas por cliente ID
     */
    List<Cuenta> findByClienteId(Long clienteId);
    
    /**
     * Obtiene cuentas por estado
     */
    List<Cuenta> findByEstado(Boolean estado);
    
    /**
     * Obtiene cuentas por estado con paginación
     */
    Page<Cuenta> findByEstado(Boolean estado, Pageable pageable);
    
    /**
     * Obtiene cuentas por tipo
     */
    List<Cuenta> findByTipoCuenta(String tipoCuenta);
    
    /**
     * Obtiene cuentas por cliente y estado
     */
    List<Cuenta> findByClienteIdAndEstado(Long clienteId, Boolean estado);
    
    /**
     * Busca cuentas por número de cuenta o tipo
     */
    @Query("SELECT c FROM Cuenta c WHERE " +
           "LOWER(c.numeroCuenta) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(c.tipoCuenta) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Cuenta> buscarCuentas(@Param("busqueda") String busqueda);
    
    /**
     * Obtiene cuentas con saldo mayor al especificado
     */
    List<Cuenta> findBySaldoActualGreaterThan(BigDecimal saldo);
    
    /**
     * Obtiene cuentas con saldo menor al especificado
     */
    List<Cuenta> findBySaldoActualLessThan(BigDecimal saldo);
    
    /**
     * Cuenta cuentas por estado
     */
    long countByEstado(Boolean estado);
    
    /**
     * Cuenta cuentas por cliente
     */
    long countByClienteId(Long clienteId);
    
    /**
     * Obtiene el saldo total de un cliente
     */
    @Query("SELECT SUM(c.saldoActual) FROM Cuenta c WHERE c.cliente.id = :clienteId AND c.estado = true")
    BigDecimal obtenerSaldoTotalPorCliente(@Param("clienteId") Long clienteId);
}
