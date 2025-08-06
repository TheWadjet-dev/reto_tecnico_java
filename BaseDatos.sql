
-- SCRIPT DE BASE DE DATOS - SISTEMA BANCARIO


-- ============================================
-- CREACIÓN DE TABLAS
-- ============================================

-- Tabla personas
CREATE TABLE personas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL CHECK (LENGTH(nombre) >= 2),
    genero VARCHAR(20),
    edad INTEGER CHECK (edad > 0 AND edad <= 120),
    identificacion VARCHAR(20) UNIQUE,
    direccion VARCHAR(200),
    telefono VARCHAR(15),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla clientes
CREATE TABLE clientes (
    persona_id BIGINT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE,
    clienteid VARCHAR(20) NOT NULL UNIQUE CHECK (LENGTH(clienteid) >= 3),
    contrasena VARCHAR(255) NOT NULL CHECK (LENGTH(contrasena) >= 6),
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla cuentas
CREATE TABLE cuentas (
    id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) NOT NULL UNIQUE CHECK (LENGTH(numero_cuenta) >= 8),
    tipo_cuenta VARCHAR(20) NOT NULL,
    saldo_inicial DECIMAL(15,2) NOT NULL CHECK (saldo_inicial >= 0),
    saldo_actual DECIMAL(15,2) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cliente_id BIGINT NOT NULL REFERENCES clientes(persona_id) ON DELETE RESTRICT
);

-- Tabla movimientos
CREATE TABLE movimientos (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL,
    descripcion VARCHAR(200),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cuenta_id BIGINT NOT NULL REFERENCES cuentas(id) ON DELETE CASCADE
);

-- Índices para mejorar performance de consultas
CREATE INDEX idx_personas_identificacion ON personas(identificacion);
CREATE INDEX idx_clientes_clienteid ON clientes(clienteid);
CREATE INDEX idx_cuentas_numero_cuenta ON cuentas(numero_cuenta);
CREATE INDEX idx_cuentas_cliente_id ON cuentas(cliente_id);
CREATE INDEX idx_cuentas_estado ON cuentas(estado);
CREATE INDEX idx_movimientos_cuenta_id ON movimientos(cuenta_id);
CREATE INDEX idx_movimientos_fecha ON movimientos(fecha);
CREATE INDEX idx_movimientos_tipo ON movimientos(tipo_movimiento);
CREATE INDEX idx_movimientos_cuenta_fecha ON movimientos(cuenta_id, fecha DESC);

    BEFORE UPDATE ON cuentas
    FOR EACH ROW
    EXECUTE FUNCTION actualizar_fecha_modificacion();

-- ============================================
-- DATOS DE EJEMPLO
-- ============================================

-- Insertar personas
INSERT INTO personas (nombre, genero, edad, identificacion, direccion, telefono) VALUES
('Juan Carlos Pérez', 'MASCULINO', 35, '1234567890', 'Calle 123 #45-67, Bogotá', '3001234567'),
('María Elena González', 'FEMENINO', 28, '0987654321', 'Carrera 50 #12-34, Medellín', '3009876543'),
('Carlos Alberto Rodríguez', 'MASCULINO', 42, '1122334455', 'Avenida 68 #23-45, Cali', '3001122334'),
('Ana Sofía Martínez', 'FEMENINO', 31, '5566778899', 'Calle 85 #15-30, Barranquilla', '3005566778'),
('Luis Fernando López', 'MASCULINO', 39, '9988776655', 'Carrera 15 #45-67, Cartagena', '3009988776');

-- Insertar clientes (referenciando personas)
INSERT INTO clientes (persona_id, clienteid, contrasena, estado) VALUES
(1, 'CLI001', '$2a$10$encrypted_password_1', TRUE),
(2, 'CLI002', '$2a$10$encrypted_password_2', TRUE),
(3, 'CLI003', '$2a$10$encrypted_password_3', TRUE),
(4, 'CLI004', '$2a$10$encrypted_password_4', TRUE),
(5, 'CLI005', '$2a$10$encrypted_password_5', FALSE);

-- Insertar cuentas
INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, estado, cliente_id) VALUES
('12345678901', 'AHORROS', 1000.00, 1500.00, TRUE, 1),
('12345678902', 'CORRIENTE', 2000.00, 2500.00, TRUE, 1),
('23456789012', 'AHORROS', 500.00, 750.00, TRUE, 2),
('34567890123', 'CORRIENTE', 3000.00, 2800.00, TRUE, 3),
('45678901234', 'AHORROS', 1500.00, 1200.00, TRUE, 4),
('56789012345', 'CORRIENTE', 800.00, 800.00, FALSE, 5);

-- Insertar movimientos
INSERT INTO movimientos (fecha, tipo_movimiento, valor, saldo, descripcion, cuenta_id) VALUES
-- Movimientos para cuenta 1 (Juan Carlos - Ahorros)
('2024-01-15 10:30:00', 'DEPOSITO', 500.00, 1500.00, 'Depósito inicial', 1),
('2024-01-20 14:15:00', 'RETIRO', -200.00, 1300.00, 'Retiro cajero automático', 1),
('2024-01-25 09:45:00', 'DEPOSITO', 200.00, 1500.00, 'Transferencia recibida', 1),

-- Movimientos para cuenta 2 (Juan Carlos - Corriente)
('2024-01-16 11:00:00', 'DEPOSITO', 300.00, 2300.00, 'Consignación cheque', 2),
('2024-01-22 16:30:00', 'RETIRO', -100.00, 2200.00, 'Pago servicio público', 2),
('2024-01-28 08:20:00', 'DEPOSITO', 300.00, 2500.00, 'Nómina empresa', 2),

-- Movimientos para cuenta 3 (María Elena - Ahorros)
('2024-01-17 15:45:00', 'DEPOSITO', 250.00, 750.00, 'Ahorro mensual', 3),
('2024-01-24 12:10:00', 'RETIRO', -50.00, 700.00, 'Compra en línea', 3),
('2024-01-30 10:05:00', 'DEPOSITO', 50.00, 750.00, 'Intereses ganados', 3),

-- Movimientos para cuenta 4 (Carlos Alberto - Corriente)
('2024-01-18 09:15:00', 'RETIRO', -200.00, 2800.00, 'Pago tarjeta de crédito', 4),
('2024-01-26 13:40:00', 'DEPOSITO', 100.00, 2900.00, 'Reintegro gastos', 4),
('2024-01-31 17:20:00', 'RETIRO', -100.00, 2800.00, 'Comisión manejo cuenta', 4),

-- Movimientos para cuenta 5 (Ana Sofía - Ahorros)
('2024-01-19 14:25:00', 'RETIRO', -300.00, 1200.00, 'Emergencia médica', 5),
('2024-01-27 11:50:00', 'DEPOSITO', 200.00, 1400.00, 'Devolución seguro', 5),
('2024-01-29 16:10:00', 'RETIRO', -200.00, 1200.00, 'Pago universidad', 5);

-- ============================================
-- CONSULTAS DE VERIFICACIÓN
-- ============================================

-- Verificar estructura de datos
SELECT 'Personas creadas:' as descripcion, COUNT(*) as cantidad FROM personas
UNION ALL
SELECT 'Clientes creados:', COUNT(*) FROM clientes
UNION ALL
SELECT 'Cuentas creadas:', COUNT(*) FROM cuentas
UNION ALL
SELECT 'Movimientos creados:', COUNT(*) FROM movimientos;

-- Verificar integridad referencial
SELECT 
    'Relación Personas-Clientes' as relacion,
    (SELECT COUNT(*) FROM clientes) as clientes,
    (SELECT COUNT(*) FROM personas p WHERE EXISTS (SELECT 1 FROM clientes c WHERE c.persona_id = p.id)) as personas_con_cliente;


COMMIT;
