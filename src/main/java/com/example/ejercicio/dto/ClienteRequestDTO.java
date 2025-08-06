package com.example.ejercicio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClienteRequestDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    
    @Size(max = 20, message = "El género no puede tener más de 20 caracteres")
    private String genero;
    
    private Integer edad;
    
    @Size(max = 20, message = "La identificación no puede tener más de 20 caracteres")
    private String identificacion;
    
    @Size(max = 200, message = "La dirección no puede tener más de 200 caracteres")
    private String direccion;
    
    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres")
    private String telefono;
    
    @NotBlank(message = "El clienteId es obligatorio")
    @Size(min = 3, max = 20, message = "El clienteId debe tener entre 3 y 20 caracteres")
    private String clienteId;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;
    
    private Boolean estado = true;
    
    public ClienteRequestDTO() {
    }
    
    // Getters and Setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public Integer getEdad() {
        return edad;
    }
    
    public void setEdad(Integer edad) {
        this.edad = edad;
    }
    
    public String getIdentificacion() {
        return identificacion;
    }
    
    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
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
}
