package com.example.ejercicio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cuentas")
public class Cuenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_cuenta", unique = true, nullable = false, length = 20)
    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(min = 8, max = 20, message = "El número de cuenta debe tener entre 8 y 20 caracteres")
    private String numeroCuenta;
    
    @Column(name = "tipo_cuenta", nullable = false, length = 20)
    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Size(max = 20, message = "El tipo de cuenta no puede tener más de 20 caracteres")
    private String tipoCuenta;
    
    @Column(name = "saldo_inicial", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0", message = "El saldo inicial no puede ser negativo")
    private BigDecimal saldoInicial;
    
    @Column(name = "saldo_actual", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoActual;
    
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Movimiento> movimientos = new ArrayList<>();
    
    public Cuenta() {
    }
    
    public Cuenta(String numeroCuenta, String tipoCuenta, BigDecimal saldoInicial, Cliente cliente) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.saldoActual = saldoInicial;
        this.cliente = cliente;
        this.estado = true;
    }
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (saldoActual == null) {
            saldoActual = saldoInicial;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
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
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public List<Movimiento> getMovimientos() {
        return movimientos;
    }
    
    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }
    
    // Utility methods
    public void addMovement(Movimiento movimiento) {
        movimientos.add(movimiento);
        movimiento.setCuenta(this);
    }
    
    public void removeMovement(Movimiento movimiento) {
        movimientos.remove(movimiento);
        movimiento.setCuenta(null);
    }
    
    public boolean hasSufficientBalance(BigDecimal monto) {
        return saldoActual.compareTo(monto.abs()) >= 0;
    }
    
    public void updateBalance(BigDecimal valor) {
        this.saldoActual = this.saldoActual.add(valor);
    }
    
    @Override
    public String toString() {
        return "Cuenta{" +
                "id=" + id +
                ", numeroCuenta='" + numeroCuenta + '\'' +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                ", saldoActual=" + saldoActual +
                ", estado=" + estado +
                '}';
    }
}
