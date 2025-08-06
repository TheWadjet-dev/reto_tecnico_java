package com.example.ejercicio.repository;

import com.example.ejercicio.model.Movimiento;
import com.example.ejercicio.model.Cuenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    
    /**
     * Obtiene movimientos por cuenta
     */
    List<Movimiento> findByCuenta(Cuenta cuenta);
    
    /**
     * Obtiene movimientos por cuenta ID
     */
    List<Movimiento> findByCuentaId(Long cuentaId);
    
    /**
     * Obtiene movimientos por cuenta ID con paginación
     */
    Page<Movimiento> findByCuentaId(Long cuentaId, Pageable pageable);
    
    /**
     * Obtiene movimientos por cuenta ordenados por fecha descendente
     */
    List<Movimiento> findByCuentaIdOrderByFechaDesc(Long cuentaId);
    
    /**
     * Obtiene movimientos por tipo
     */
    List<Movimiento> findByTipoMovimiento(String tipoMovimiento);
    
    /**
     * Obtiene movimientos por rango de fechas
     */
    List<Movimiento> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Obtiene movimientos por cuenta y rango de fechas
     */
    List<Movimiento> findByCuentaIdAndFechaBetween(Long cuentaId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Obtiene movimientos por cuenta y tipo
     */
    List<Movimiento> findByCuentaIdAndTipoMovimiento(Long cuentaId, String tipoMovimiento);
    
    /**
     * Obtiene movimientos por valor mayor al especificado
     */
    List<Movimiento> findByValorGreaterThan(BigDecimal valor);
    
    /**
     * Obtiene movimientos por valor menor al especificado
     */
    List<Movimiento> findByValorLessThan(BigDecimal valor);
    
    /**
     * Busca movimientos por descripción
     */
    @Query("SELECT m FROM Movimiento m WHERE " +
           "LOWER(m.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(m.tipoMovimiento) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Movimiento> buscarMovimientos(@Param("busqueda") String busqueda);
    
    /**
     * Cuenta movimientos por cuenta
     */
    long countByCuentaId(Long cuentaId);
    
    /**
     * Cuenta movimientos por tipo
     */
    long countByTipoMovimiento(String tipoMovimiento);
    
    /**
     * Obtiene el último movimiento de una cuenta
     */
    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.id = :cuentaId ORDER BY m.fecha DESC LIMIT 1")
    Movimiento findUltimoMovimientoPorCuenta(@Param("cuentaId") Long cuentaId);
    
    /**
     * Obtiene movimientos de débito por cuenta
     */
    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.id = :cuentaId AND m.valor < 0 ORDER BY m.fecha DESC")
    List<Movimiento> findMovimientosDebitoPorCuenta(@Param("cuentaId") Long cuentaId);
    
    /**
     * Obtiene movimientos de crédito por cuenta
     */
    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.id = :cuentaId AND m.valor > 0 ORDER BY m.fecha DESC")
    List<Movimiento> findMovimientosCreditoPorCuenta(@Param("cuentaId") Long cuentaId);
    
    /**
     * Obtiene la suma de movimientos por cuenta y tipo
     */
    @Query("SELECT SUM(m.valor) FROM Movimiento m WHERE m.cuenta.id = :cuentaId AND m.tipoMovimiento = :tipo")
    BigDecimal sumValorPorCuentaYTipo(@Param("cuentaId") Long cuentaId, @Param("tipo") String tipo);
}
