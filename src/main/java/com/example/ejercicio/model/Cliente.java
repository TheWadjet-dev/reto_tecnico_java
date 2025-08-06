package com.example.ejercicio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "persona_id")
public class Cliente extends Persona {
    
    @Column(name = "clienteid", unique = true, nullable = false, length = 20)
    @NotBlank(message = "El clienteId es obligatorio")
    @Size(min = 3, max = 20, message = "El clienteId debe tener entre 3 y 20 caracteres")
    private String clienteId;
    
    @Column(name = "contrasena", nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;
    
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cuenta> cuentas = new ArrayList<>();
    
    // Constructores
    public Cliente() {
        super();
    }
    
    public Cliente(String nombre, String genero, Integer edad, String identificacion, 
                   String direccion, String telefono, String clienteId, String contrasena) {
        super(nombre, genero, edad, identificacion, direccion, telefono);
        this.clienteId = clienteId;
        this.contrasena = contrasena;
        this.estado = true;
    }
    
    // Getters y Setters
    public String getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public Boolean getEstado() {
        return estado;
    }
    
    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
    
    public List<Cuenta> getCuentas() {
        return cuentas;
    }
    
    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }
    
    // Utility methods
    public void addAccount(Cuenta cuenta) {
        cuentas.add(cuenta);
        cuenta.setCliente(this);
    }
    
    public void removeAccount(Cuenta cuenta) {
        cuentas.remove(cuenta);
        cuenta.setCliente(null);
    }
    
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", clienteId='" + clienteId + '\'' +
                ", estado=" + estado +
                '}';
    }
}
