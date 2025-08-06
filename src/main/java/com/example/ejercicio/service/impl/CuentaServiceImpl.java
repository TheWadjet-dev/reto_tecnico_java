package com.example.ejercicio.service.impl;

import com.example.ejercicio.dto.CuentaRequestDTO;
import com.example.ejercicio.dto.CuentaResponseDTO;
import com.example.ejercicio.exception.ResourceNotFoundException;
import com.example.ejercicio.exception.DuplicateResourceException;
import com.example.ejercicio.model.Cliente;
import com.example.ejercicio.model.Cuenta;
import com.example.ejercicio.repository.ClienteRepository;
import com.example.ejercicio.repository.CuentaRepository;
import com.example.ejercicio.service.CuentaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CuentaServiceImpl implements CuentaService {

    private static final String CLIENT_NOT_FOUND_MESSAGE = "Cliente no encontrado con ID: ";
    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Cuenta no encontrada con ID: ";
    private static final String ACCOUNT_NOT_FOUND_BY_NUMBER_MESSAGE = "Cuenta no encontrada con número: ";
    private static final String DUPLICATE_ACCOUNT_NUMBER_MESSAGE = "Ya existe una cuenta con el número: ";
    private static final String ACCOUNT_HAS_MOVEMENTS_MESSAGE = "No se puede eliminar la cuenta porque tiene movimientos asociados";
    private static final String INSUFFICIENT_BALANCE_MESSAGE = "Saldo insuficiente para realizar la operación";

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, ClienteRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public CuentaResponseDTO createAccount(CuentaRequestDTO cuentaRequestDTO) {
        // Verify client exists
        Cliente cliente = clienteRepository.findById(cuentaRequestDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException(CLIENT_NOT_FOUND_MESSAGE + cuentaRequestDTO.getClienteId()));
        
        // Check if account number already exists
        if (cuentaRepository.existsByNumeroCuenta(cuentaRequestDTO.getNumeroCuenta())) {
            throw new DuplicateResourceException(DUPLICATE_ACCOUNT_NUMBER_MESSAGE + cuentaRequestDTO.getNumeroCuenta());
        }
        
        // Create new account
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(cuentaRequestDTO.getNumeroCuenta());
        cuenta.setTipoCuenta(cuentaRequestDTO.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaRequestDTO.getSaldoInicial());
        cuenta.setSaldoActual(cuentaRequestDTO.getSaldoInicial());
        cuenta.setEstado(true);
        cuenta.setCliente(cliente);
        
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        return convertToResponseDTO(cuentaGuardada);
    }
    
    @Override
    public CuentaResponseDTO getAccountById(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE + id));
        return convertToResponseDTO(cuenta);
    }
    
    @Override
    public CuentaResponseDTO getAccountByNumber(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND_BY_NUMBER_MESSAGE + numeroCuenta));
        return convertToResponseDTO(cuenta);
    }
    
    @Override
    public List<CuentaResponseDTO> getAllAccounts() {
        return cuentaRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<CuentaResponseDTO> getAccountsPaginated(Pageable pageable) {
        return cuentaRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }
    
    @Override
    public List<CuentaResponseDTO> getActiveAccounts() {
        return cuentaRepository.findByEstado(true).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CuentaResponseDTO> getAccountsByClient(Long clienteId) {
        // Verify client exists
        clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(CLIENT_NOT_FOUND_MESSAGE + clienteId));
        
        return cuentaRepository.findByClienteId(clienteId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CuentaResponseDTO> getActiveAccountsByClient(Long clienteId) {
        // Verify client exists
        clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(CLIENT_NOT_FOUND_MESSAGE + clienteId));
        
        return cuentaRepository.findByClienteIdAndEstado(clienteId, true).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public CuentaResponseDTO updateAccount(Long id, CuentaRequestDTO cuentaRequestDTO) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE + id));
        
        // Check if account number is being changed and if it already exists
        if (!cuenta.getNumeroCuenta().equals(cuentaRequestDTO.getNumeroCuenta()) &&
            cuentaRepository.existsByNumeroCuenta(cuentaRequestDTO.getNumeroCuenta())) {
            throw new DuplicateResourceException(DUPLICATE_ACCOUNT_NUMBER_MESSAGE + cuentaRequestDTO.getNumeroCuenta());
        }
        
        // Update account data
        cuenta.setNumeroCuenta(cuentaRequestDTO.getNumeroCuenta());
        cuenta.setTipoCuenta(cuentaRequestDTO.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaRequestDTO.getSaldoInicial());
        
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
        return convertToResponseDTO(cuentaActualizada);
    }
    
    @Override
    public void deleteAccount(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE + id));
        
        // Soft delete - change status to false
        cuenta.setEstado(false);
        cuentaRepository.save(cuenta);
    }
    
    @Override
    public CuentaResponseDTO activateAccount(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE + id));
        
        cuenta.setEstado(true);
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        return convertToResponseDTO(cuentaGuardada);
    }
    
    @Override
    public List<CuentaResponseDTO> searchAccounts(String busqueda) {
        return cuentaRepository.buscarCuentas(busqueda).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsAccountByNumber(String numeroCuenta) {
        return cuentaRepository.existsByNumeroCuenta(numeroCuenta);
    }
    
    @Override
    public BigDecimal getTotalBalanceByClient(Long clienteId) {
        // Verify client exists
        clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(CLIENT_NOT_FOUND_MESSAGE + clienteId));
        
        List<Cuenta> cuentas = cuentaRepository.findByClienteIdAndEstado(clienteId, true);
        return cuentas.stream()
                .map(Cuenta::getSaldoActual)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Override
    public long countActiveAccounts() {
        return cuentaRepository.countByEstado(true);
    }
    
    @Override
    public List<CuentaResponseDTO> getAccountsByType(String tipoCuenta) {
        return cuentaRepository.findByTipoCuenta(tipoCuenta).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CuentaResponseDTO> getAccountsWithMinimumBalance(BigDecimal saldoMinimo) {
        return cuentaRepository.findBySaldoActualGreaterThan(saldoMinimo).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public CuentaResponseDTO changeAccountStatus(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE + id));
        
        // Toggle status
        cuenta.setEstado(!cuenta.getEstado());
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
        return convertToResponseDTO(cuentaActualizada);
    }
    
    @Override
    public BigDecimal getAccountBalance(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE + id));
        return cuenta.getSaldoActual();
    }
    
    @Override
    public long countMovementsByAccount(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE + id));
        
        // Use repository to get account and count its movements
        return cuenta.getMovimientos().size();
    }
    
    /**
     * Converts a Cuenta entity to a CuentaResponseDTO
     */
    private CuentaResponseDTO convertToResponseDTO(Cuenta cuenta) {
        CuentaResponseDTO responseDTO = new CuentaResponseDTO();
        responseDTO.setId(cuenta.getId());
        responseDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
        responseDTO.setTipoCuenta(cuenta.getTipoCuenta());
        responseDTO.setSaldoInicial(cuenta.getSaldoInicial());
        responseDTO.setSaldoActual(cuenta.getSaldoActual());
        responseDTO.setEstado(cuenta.getEstado());
        responseDTO.setFechaCreacion(cuenta.getFechaCreacion());
        responseDTO.setFechaActualizacion(cuenta.getFechaActualizacion());
        
        // Add client information
        if (cuenta.getCliente() != null) {
            responseDTO.setClienteId(cuenta.getCliente().getId());
            responseDTO.setClienteNombre(cuenta.getCliente().getNombre());
        }
        
        return responseDTO;
    }
}
