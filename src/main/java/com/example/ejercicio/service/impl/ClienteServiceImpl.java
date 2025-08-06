package com.example.ejercicio.service.impl;

import com.example.ejercicio.dto.ClienteRequestDTO;
import com.example.ejercicio.dto.ClienteResponseDTO;
import com.example.ejercicio.exception.DuplicateResourceException;
import com.example.ejercicio.exception.ResourceNotFoundException;
import com.example.ejercicio.model.Cliente;
import com.example.ejercicio.repository.ClienteRepository;
import com.example.ejercicio.repository.CuentaRepository;
import com.example.ejercicio.service.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {
    
    private static final String CLIENT_NOT_FOUND_MESSAGE = "Cliente no encontrado con ID: ";
    private static final String CLIENT_NOT_FOUND_BY_CLIENT_ID_MESSAGE = "Cliente no encontrado con clienteId: ";
    private static final String CLIENT_NOT_FOUND_BY_IDENTIFICATION_MESSAGE = "Cliente no encontrado con identificación: ";
    private static final String DUPLICATE_CLIENT_ID_MESSAGE = "Ya existe un cliente con el clienteId: ";
    private static final String DUPLICATE_IDENTIFICATION_MESSAGE = "Ya existe un cliente con la identificación: ";
    private static final String CLIENT_HAS_ACCOUNTS_MESSAGE = "No se puede eliminar el cliente porque tiene cuentas asociadas";
    
    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;
    
    public ClienteServiceImpl(ClienteRepository clienteRepository, CuentaRepository cuentaRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
    }
    
    @Override
    public ClienteResponseDTO createClient(ClienteRequestDTO clienteRequestDTO) {
        if (clienteRepository.existsByClienteId(clienteRequestDTO.getClienteId())) {
            throw new DuplicateResourceException(DUPLICATE_CLIENT_ID_MESSAGE + clienteRequestDTO.getClienteId());
        }
        
        if (clienteRequestDTO.getIdentificacion() != null && 
            clienteRepository.existsByIdentificacion(clienteRequestDTO.getIdentificacion())) {
            throw new DuplicateResourceException(DUPLICATE_IDENTIFICATION_MESSAGE + clienteRequestDTO.getIdentificacion());
        }
        
        Cliente cliente = new Cliente(
                clienteRequestDTO.getNombre(),
                clienteRequestDTO.getGenero(),
                clienteRequestDTO.getEdad(),
                clienteRequestDTO.getIdentificacion(),
                clienteRequestDTO.getDireccion(),
                clienteRequestDTO.getTelefono(),
                clienteRequestDTO.getClienteId(),
                clienteRequestDTO.getContrasena()
        );
        
        cliente.setEstado(clienteRequestDTO.getEstado());
        cliente = clienteRepository.save(cliente);
        
        return convertirAClienteResponseDTO(cliente);
    }
    
    @Override
    public ClienteResponseDTO getClientById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CLIENT_NOT_FOUND_MESSAGE + id));
        return convertirAClienteResponseDTO(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO getClientByClienteId(String clienteId) {
        Cliente cliente = clienteRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(CLIENT_NOT_FOUND_BY_CLIENT_ID_MESSAGE + clienteId));
        return convertirAClienteResponseDTO(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> getAllClients() {
        return clienteRepository.findAll().stream()
                .map(this::convertirAClienteResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> getClientsPaginated(Pageable pageable) {
        return clienteRepository.findAll(pageable)
                .map(this::convertirAClienteResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> getActiveClients() {
        return clienteRepository.findByEstado(true).stream()
                .map(this::convertirAClienteResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> getActiveClientsPaginated(Pageable pageable) {
        return clienteRepository.findByEstado(true, pageable)
                .map(this::convertirAClienteResponseDTO);
    }
    
    @Override
    public ClienteResponseDTO updateClient(Long id, ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CLIENT_NOT_FOUND_MESSAGE + id));
        
        if (!cliente.getClienteId().equals(clienteRequestDTO.getClienteId()) &&
            clienteRepository.existsByClienteId(clienteRequestDTO.getClienteId())) {
            throw new DuplicateResourceException(DUPLICATE_CLIENT_ID_MESSAGE + clienteRequestDTO.getClienteId());
        }
        
        if (clienteRequestDTO.getIdentificacion() != null &&
            !clienteRequestDTO.getIdentificacion().equals(cliente.getIdentificacion()) &&
            clienteRepository.existsByIdentificacion(clienteRequestDTO.getIdentificacion())) {
            throw new DuplicateResourceException(DUPLICATE_IDENTIFICATION_MESSAGE + clienteRequestDTO.getIdentificacion());
        }
        
        cliente.setNombre(clienteRequestDTO.getNombre());
        cliente.setGenero(clienteRequestDTO.getGenero());
        cliente.setEdad(clienteRequestDTO.getEdad());
        cliente.setIdentificacion(clienteRequestDTO.getIdentificacion());
        cliente.setDireccion(clienteRequestDTO.getDireccion());
        cliente.setTelefono(clienteRequestDTO.getTelefono());
        cliente.setClienteId(clienteRequestDTO.getClienteId());
        cliente.setContrasena(clienteRequestDTO.getContrasena());
        cliente.setEstado(clienteRequestDTO.getEstado());
        
        cliente = clienteRepository.save(cliente);
        return convertirAClienteResponseDTO(cliente);
    }
    
    @Override
    public void deleteClient(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CLIENT_NOT_FOUND_MESSAGE + id));
        
        long cuentasCount = cuentaRepository.countByClienteId(id);
        if (cuentasCount > 0) {
            throw new IllegalStateException("No se puede eliminar el cliente: tiene " + cuentasCount + " cuenta(s) asociada(s)");
        }
        
        cliente.setEstado(false);
        clienteRepository.save(cliente);
    }
    
    @Override
    public ClienteResponseDTO activateClient(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CLIENT_NOT_FOUND_MESSAGE + id));
        
        cliente.setEstado(true);
        cliente = clienteRepository.save(cliente);
        return convertirAClienteResponseDTO(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> searchClients(String busqueda) {
        return clienteRepository.buscarClientes(busqueda).stream()
                .map(this::convertirAClienteResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsClientByClienteId(String clienteId) {
        return clienteRepository.existsByClienteId(clienteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsClientByIdentification(String identificacion) {
        return clienteRepository.existsByIdentificacion(identificacion);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveClients() {
        return clienteRepository.countByEstado(true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO getClientByIdentification(String identificacion) {
        Cliente cliente = clienteRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new ResourceNotFoundException(CLIENT_NOT_FOUND_BY_IDENTIFICATION_MESSAGE + identificacion));
        return convertirAClienteResponseDTO(cliente);
    }
    
    @Override
    public ClienteResponseDTO changeClientStatus(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CLIENT_NOT_FOUND_MESSAGE + id));
        
        cliente.setEstado(!cliente.getEstado());
        cliente = clienteRepository.save(cliente);
        return convertirAClienteResponseDTO(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countAccountsByClient(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException(CLIENT_NOT_FOUND_MESSAGE + id);
        }
        return cuentaRepository.countByClienteId(id);
    }
    
    private ClienteResponseDTO convertirAClienteResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getGenero(),
                cliente.getEdad(),
                cliente.getIdentificacion(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getClienteId(),
                cliente.getEstado(),
                cliente.getFechaCreacion(),
                cliente.getFechaActualizacion()
        );
    }
}
