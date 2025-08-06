package com.example.ejercicio.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class CuentaRequestDTO {
    
    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(min = 8, max = 20, message = "El número de cuenta debe tener entre 8 y 20 caracteres")
    private String numeroCuenta;
    
    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Size(max = 20, message = "El tipo de cuenta no puede tener más de 20 caracteres")
    private String tipoCuenta;
    
    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0", message = "El saldo inicial no puede ser negativo")
    private BigDecimal saldoInicial;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;
    
    private Boolean estado = true;
    
    public CuentaRequestDTO() {
    }
    
    // Getters and Setters
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
    
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public Boolean getEstado() {
        return estado;
    }
    
    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
