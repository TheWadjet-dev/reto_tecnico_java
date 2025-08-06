package com.example.ejercicio.repository;

import com.example.ejercicio.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    /**
     * Busca un cliente por su clienteId
     */
    Optional<Cliente> findByClienteId(String clienteId);
    
    /**
     * Busca un cliente por su identificación
     */
    Optional<Cliente> findByIdentificacion(String identificacion);
    
    /**
     * Verifica si existe un cliente con el clienteId dado
     */
    boolean existsByClienteId(String clienteId);
    
    /**
     * Verifica si existe un cliente con la identificación dada
     */
    boolean existsByIdentificacion(String identificacion);
    
    /**
     * Obtiene clientes por estado
     */
    List<Cliente> findByEstado(Boolean estado);
    
    /**
     * Obtiene clientes por estado con paginación
     */
    Page<Cliente> findByEstado(Boolean estado, Pageable pageable);
    
    /**
     * Busca clientes por nombre, clienteId o identificación
     */
    @Query("SELECT c FROM Cliente c WHERE " +
           "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(c.clienteId) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(c.identificacion) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Cliente> buscarClientes(@Param("busqueda") String busqueda);
    
    /**
     * Cuenta clientes activos
     */
    long countByEstado(Boolean estado);
    
    /**
     * Obtiene clientes por rango de edad
     */
    List<Cliente> findByEdadBetween(Integer edadMin, Integer edadMax);
    
    /**
     * Obtiene clientes por género
     */
    List<Cliente> findByGenero(String genero);
}
