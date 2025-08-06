package com.example.ejercicio.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CuentaResponseDTO {
    
    private Long id;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private BigDecimal saldoActual;
    private Boolean estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Long clienteId;
    private String clienteNombre;
    
    public CuentaResponseDTO() {
    }
    
    public CuentaResponseDTO(Long id, String numeroCuenta, String tipoCuenta, 
                            BigDecimal saldoInicial, BigDecimal saldoActual, Boolean estado, 
                            LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion, 
                            Long clienteId, String clienteNombre) {
        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.saldoActual = saldoActual;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNumeroCuenta() {
        return numeroCuenta;
    }
    
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
    
    public String getTipoCuenta() {
        return tipoCuenta;
    }
    
    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
    
    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }
    
    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }
    
    public BigDecimal getSaldoActual() {
        return saldoActual;
    }
    
    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldoActual = saldoActual;
    }
    
    public Boolean getEstado() {
        return estado;
    }
    
    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getClienteNombre() {
        return clienteNombre;
    }
    
    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }
}
