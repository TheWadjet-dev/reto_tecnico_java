package com.example.ejercicio.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoRequestDTO {
    
    private LocalDateTime fecha;
    
    @Size(max = 20, message = "El tipo de movimiento no puede tener más de 20 caracteres")
    private String tipoMovimiento;
    
    @NotNull(message = "El valor es obligatorio")
    private BigDecimal valor;
    
    @Size(max = 200, message = "La descripción no puede tener más de 200 caracteres")
    private String descripcion;
    
    @NotNull(message = "El ID de la cuenta es obligatorio")
    private Long cuentaId;
    
    public MovimientoRequestDTO() {
    }
    
    // Getters and Setters
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
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Long getCuentaId() {
        return cuentaId;
    }
    
    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }
}
