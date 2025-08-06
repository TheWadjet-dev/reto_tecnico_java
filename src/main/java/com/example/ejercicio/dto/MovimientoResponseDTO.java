package com.example.ejercicio.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoResponseDTO {
    
    private Long id;
    private LocalDateTime fecha;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private Long cuentaId;
    private String numeroCuenta;
    private String clienteNombre;
    
    public MovimientoResponseDTO() {
    }
    
    public MovimientoResponseDTO(Long id, LocalDateTime fecha, String tipoMovimiento, 
                               BigDecimal valor, BigDecimal saldo, String descripcion, 
                               LocalDateTime fechaCreacion, Long cuentaId, String numeroCuenta, 
                               String clienteNombre) {
        this.id = id;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.cuentaId = cuentaId;
        this.numeroCuenta = numeroCuenta;
        this.clienteNombre = clienteNombre;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public String getTipoMovimiento() {
        return tipoMovimiento;
    }
    
    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public BigDecimal getSaldo() {
        return saldo;
    }
    
    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public Long getCuentaId() {
        return cuentaId;
    }
    
    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }
    
    public String getNumeroCuenta() {
        return numeroCuenta;
    }
    
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
    
    public String getClienteNombre() {
        return clienteNombre;
    }
    
    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }
}
