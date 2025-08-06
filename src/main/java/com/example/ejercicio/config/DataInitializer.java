package com.example.ejercicio.config;

import com.example.ejercicio.model.Cliente;
import com.example.ejercicio.model.Cuenta;
import com.example.ejercicio.model.Movimiento;
import com.example.ejercicio.repository.ClienteRepository;
import com.example.ejercicio.repository.CuentaRepository;
import com.example.ejercicio.repository.MovimientoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final String SAVINGS_ACCOUNT_TYPE = "AHORRO";
    private static final String CHECKING_ACCOUNT_TYPE = "CORRIENTE";
    private static final String CREDIT_MOVEMENT_TYPE = "CREDITO";
    private static final String DEBIT_MOVEMENT_TYPE = "DEBITO";
    private static final String FEMALE_GENDER = "FEMENINO";
    private static final String MALE_GENDER = "MASCULINO";
    
    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    
    public DataInitializer(ClienteRepository clienteRepository, CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        if (shouldInitializeData()) {
            initializeTestData();
        }
    }
    
    private boolean shouldInitializeData() {
        return clienteRepository.count() == 0;
    }
    
    private void initializeTestData() {
        Cliente[] clients = createAndSaveClients();
        Cuenta[] accounts = createAndSaveAccounts(clients);
        createAndSaveMovements(accounts);
    }
    
    private Cliente[] createAndSaveClients() {
        Cliente cliente1 = createClient("María García", FEMALE_GENDER, 28, "1234567890", 
                                      "Av. Libertador 123, Caracas", "+58-212-1234567", "CLI001", "password123", true);
        
        Cliente cliente2 = createClient("Carlos Rodríguez", MALE_GENDER, 35, "0987654321", 
                                      "Calle Real 456, Valencia", "+58-241-9876543", "CLI002", "password456", true);
        
        Cliente cliente3 = createClient("Ana Martínez", FEMALE_GENDER, 42, "1122334455", 
                                      "Urbanización El Rosal, Maracaibo", "+58-261-5555666", "CLI003", "password789", true);
        
        Cliente cliente4 = createClient("Pedro López", MALE_GENDER, 30, "5566778899", 
                                      "Centro Comercial Las Mercedes, Caracas", "+58-212-7777888", "CLI004", "password000", false);
        
        // Save clients
        cliente1 = clienteRepository.save(cliente1);
        cliente2 = clienteRepository.save(cliente2);
        cliente3 = clienteRepository.save(cliente3);
        cliente4 = clienteRepository.save(cliente4);
        
        return new Cliente[]{cliente1, cliente2, cliente3, cliente4};
    }
    
    private Cliente createClient(String nombre, String genero, int edad, String identificacion, 
                               String direccion, String telefono, String clienteId, String password, boolean estado) {
        Cliente cliente = new Cliente(nombre, genero, edad, identificacion, direccion, telefono, clienteId, password);
        cliente.setEstado(estado);
        return cliente;
    }
    
    private Cuenta[] createAndSaveAccounts(Cliente[] clients) {
        Cuenta cuenta1 = createAccount("4001234567890001", SAVINGS_ACCOUNT_TYPE, new BigDecimal("1000.00"), 
                                     new BigDecimal("1250.00"), clients[0], true);
        
        Cuenta cuenta2 = createAccount("4001234567890002", CHECKING_ACCOUNT_TYPE, new BigDecimal("2000.00"), 
                                     new BigDecimal("1800.00"), clients[0], true);
        
        Cuenta cuenta3 = createAccount("4000987654321001", SAVINGS_ACCOUNT_TYPE, new BigDecimal("5000.00"), 
                                     new BigDecimal("5500.00"), clients[1], true);
        
        Cuenta cuenta4 = createAccount("4001122334455001", CHECKING_ACCOUNT_TYPE, new BigDecimal("3000.00"), 
                                     new BigDecimal("2750.00"), clients[2], true);
        
        Cuenta cuenta5 = createAccount("4005566778899001", SAVINGS_ACCOUNT_TYPE, new BigDecimal("1500.00"), 
                                     new BigDecimal("1500.00"), clients[3], false);
        
        // Save accounts
        cuenta1 = cuentaRepository.save(cuenta1);
        cuenta2 = cuentaRepository.save(cuenta2);
        cuenta3 = cuentaRepository.save(cuenta3);
        cuenta4 = cuentaRepository.save(cuenta4);
        cuenta5 = cuentaRepository.save(cuenta5);
        
        return new Cuenta[]{cuenta1, cuenta2, cuenta3, cuenta4, cuenta5};
    }
    
    private Cuenta createAccount(String numeroCuenta, String tipoCuenta, BigDecimal saldoInicial, 
                               BigDecimal saldoActual, Cliente cliente, boolean estado) {
        Cuenta cuenta = new Cuenta(numeroCuenta, tipoCuenta, saldoInicial, cliente);
        cuenta.setSaldoActual(saldoActual);
        cuenta.setEstado(estado);
        return cuenta;
    }
    
    private void createAndSaveMovements(Cuenta[] accounts) {
        LocalDateTime now = LocalDateTime.now();
        
        createMovementsForAccount(accounts[0], now); // María - Ahorro
        createMovementsForAccount2(accounts[1], now); // María - Corriente
        createMovementsForAccount3(accounts[2], now); // Carlos - Ahorro
        createMovementsForAccount4(accounts[3], now); // Ana - Corriente
    }
    
    private void createMovementsForAccount(Cuenta cuenta, LocalDateTime now) {
        Movimiento mov1 = createMovement(now.minusDays(5), CREDIT_MOVEMENT_TYPE, new BigDecimal("250.00"), 
                                       new BigDecimal("1250.00"), "Depósito inicial", cuenta);
        
        Movimiento mov2 = createMovement(now.minusDays(3), DEBIT_MOVEMENT_TYPE, new BigDecimal("-100.00"), 
                                       new BigDecimal("1150.00"), "Retiro cajero automático", cuenta);
        
        Movimiento mov3 = createMovement(now.minusDays(1), CREDIT_MOVEMENT_TYPE, new BigDecimal("100.00"), 
                                       new BigDecimal("1250.00"), "Transferencia recibida", cuenta);
        
        saveMovements(mov1, mov2, mov3);
    }
    
    private void createMovementsForAccount2(Cuenta cuenta, LocalDateTime now) {
        Movimiento mov4 = createMovement(now.minusDays(4), DEBIT_MOVEMENT_TYPE, new BigDecimal("-200.00"), 
                                       new BigDecimal("1800.00"), "Pago servicios", cuenta);
        
        saveMovements(mov4);
    }
    
    private void createMovementsForAccount3(Cuenta cuenta, LocalDateTime now) {
        Movimiento mov5 = createMovement(now.minusDays(7), CREDIT_MOVEMENT_TYPE, new BigDecimal("500.00"), 
                                       new BigDecimal("5500.00"), "Depósito nómina", cuenta);
        
        Movimiento mov6 = createMovement(now.minusDays(2), DEBIT_MOVEMENT_TYPE, new BigDecimal("-300.00"), 
                                       new BigDecimal("5200.00"), "Compra supermercado", cuenta);
        
        Movimiento mov7 = createMovement(now.minusDays(1), CREDIT_MOVEMENT_TYPE, new BigDecimal("300.00"), 
                                       new BigDecimal("5500.00"), "Devolución compra", cuenta);
        
        saveMovements(mov5, mov6, mov7);
    }
    
    private void createMovementsForAccount4(Cuenta cuenta, LocalDateTime now) {
        Movimiento mov8 = createMovement(now.minusDays(6), DEBIT_MOVEMENT_TYPE, new BigDecimal("-250.00"), 
                                       new BigDecimal("2750.00"), "Pago tarjeta crédito", cuenta);
        
        Movimiento mov9 = createMovement(now.minusDays(4), CREDIT_MOVEMENT_TYPE, new BigDecimal("100.00"), 
                                       new BigDecimal("2850.00"), "Transferencia familiar", cuenta);
        
        Movimiento mov10 = createMovement(now.minusDays(2), DEBIT_MOVEMENT_TYPE, new BigDecimal("-100.00"), 
                                        new BigDecimal("2750.00"), "Retiro cajero", cuenta);
        
        saveMovements(mov8, mov9, mov10);
    }
    
    private Movimiento createMovement(LocalDateTime fecha, String tipoMovimiento, BigDecimal valor, 
                                    BigDecimal saldo, String descripcion, Cuenta cuenta) {
        return new Movimiento(fecha, tipoMovimiento, valor, saldo, descripcion, cuenta);
    }
    
    private void saveMovements(Movimiento... movimientos) {
        for (Movimiento movimiento : movimientos) {
            movimientoRepository.save(movimiento);
        }
    }
}
