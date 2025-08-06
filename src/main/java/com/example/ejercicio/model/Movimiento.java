package com.example.ejercicio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "fecha", nullable = false)
    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;
    
    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Size(max = 20, message = "El tipo de movimiento no puede tener más de 20 caracteres")
    private String tipoMovimiento;
    
    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "El valor es obligatorio")
    private BigDecimal valor;
    
    @Column(name = "saldo", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;
    
    @Column(name = "descripcion", length = 200)
    @Size(max = 200, message = "La descripción no puede tener más de 200 caracteres")
    private String descripcion;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;
    
    public Movimiento() {
    }
    
    public Movimiento(LocalDateTime fecha, String tipoMovimiento, BigDecimal valor, 
                     BigDecimal saldo, String descripcion, Cuenta cuenta) {
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.descripcion = descripcion;
        this.cuenta = cuenta;
    }
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
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
    
    public Cuenta getCuenta() {
        return cuenta;
    }
    
    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
    
    // Utility methods
    public boolean isDebit() {
        return valor.compareTo(BigDecimal.ZERO) < 0;
    }
    
    public boolean isCredit() {
        return valor.compareTo(BigDecimal.ZERO) > 0;
    }
    
    @Override
    public String toString() {
        return "Movimiento{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", tipoMovimiento='" + tipoMovimiento + '\'' +
                ", valor=" + valor +
                ", saldo=" + saldo +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
