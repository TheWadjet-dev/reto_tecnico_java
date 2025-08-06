package com.example.ejercicio.service;

import com.example.ejercicio.dto.ClienteRequestDTO;
import com.example.ejercicio.dto.ClienteResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClienteService {
    
    /**
     * Creates a new client
     */
    ClienteResponseDTO createClient(ClienteRequestDTO clienteRequestDTO);
    
    /**
     * Gets a client by ID
     */
    ClienteResponseDTO getClientById(Long id);
    
    /**
     * Gets a client by clienteId
     */
    ClienteResponseDTO getClientByClienteId(String clienteId);
    
    /**
     * Gets all clients
     */
    List<ClienteResponseDTO> getAllClients();
    
    /**
     * Gets clients with pagination
     */
    Page<ClienteResponseDTO> getClientsPaginated(Pageable pageable);
    
    /**
     * Gets active clients
     */
    List<ClienteResponseDTO> getActiveClients();
    
    /**
     * Gets active clients with pagination
     */
    Page<ClienteResponseDTO> getActiveClientsPaginated(Pageable pageable);
    
    /**
     * Updates a client
     */
    ClienteResponseDTO updateClient(Long id, ClienteRequestDTO clienteRequestDTO);
    
    /**
     * Deletes a client (soft delete)
     */
    void deleteClient(Long id);
    
    /**
     * Activates a client
     */
    ClienteResponseDTO activateClient(Long id);
    
    /**
     * Searches clients by search term
     */
    List<ClienteResponseDTO> searchClients(String busqueda);
    
    /**
     * Checks if a client exists with the given clienteId
     */
    boolean existsClientByClienteId(String clienteId);
    
    /**
     * Checks if a client exists with the given identification
     */
    boolean existsClientByIdentification(String identificacion);
    
    /**
     * Counts active clients
     */
    long countActiveClients();
    
    /**
     * Gets a client by identification
     */
    ClienteResponseDTO getClientByIdentification(String identificacion);
    
    /**
     * Changes client status (active/inactive)
     */
    ClienteResponseDTO changeClientStatus(Long id);
    
    /**
     * Counts accounts for a client
     */
    long countAccountsByClient(Long id);
}
