# Ejercicio Técnico Backend Java

Una API REST completa desarrollada con Spring Boot que demuestra las habilidades esenciales de desarrollo backend Java a nivel junior.

## Características Principales

### Funcionalidades Implementadas

- **CRUD Completo**: Crear, leer, actualizar y eliminar usuarios
- **API REST**: Endpoints RESTful siguiendo las mejores prácticas
- **Validación de Datos**: Validación completa usando Bean Validation
- **Manejo de Excepciones**: Manejo global de errores con respuestas consistentes
- **Base de Datos**: Integración con JPA/Hibernate y base de datos H2
- **Seguridad**: Configuración básica de Spring Security
- **Documentación**: API documentada con Swagger/OpenAPI
- **Paginación**: Soporte para paginación y ordenamiento
- **Búsqueda**: Funcionalidad de búsqueda de usuarios
- **Soft Delete**: Eliminación lógica de registros
- **Datos de Prueba**: Carga automática de datos de ejemplo
- **Testing**: Pruebas unitarias con JUnit y Mockito

### Arquitectura

```
├── controller/     # Controladores REST
├── service/        # Lógica de negocio
├── repository/     # Acceso a datos
├── model/          # Entidades JPA
├── dto/            # Objetos de transferencia de datos
├── exception/      # Manejo de excepciones
└── config/         # Configuración de la aplicación
```

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA** - Persistencia de datos
- **Spring Security** - Seguridad
- **Spring Validation** - Validación de datos
- **H2 Database** - Base de datos en memoria
- **Swagger/OpenAPI 3** - Documentación de API
- **Maven** - Gestión de dependencias
- **JUnit 5** - Testing
- **Mockito** - Mocking para pruebas

## Inicio Rápido

### Prerrequisitos

- Java 17 o superior
- Maven 3.6 o superior

### Instalación y Ejecución

1. **Clonar el repositorio**
   ```bash
   git clone <repository-url>
   cd ejercicio-tecnico
   ```

2. **Compilar el proyecto**
   ```bash
   mvn clean compile
   ```

3. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

4. **Verificar que la aplicación esté funcionando**
   ```bash
   curl http://localhost:8080/api/users
   ```

La aplicación estará disponible en: `http://localhost:8080`

## Documentación de la API

### Swagger UI
Una vez que la aplicación esté ejecutándose, puedes acceder a la documentación interactiva de la API en:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Endpoints Principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/users` | Obtener todos los usuarios |
| GET | `/api/users/{id}` | Obtener usuario por ID |
| POST | `/api/users` | Crear nuevo usuario |
| PUT | `/api/users/{id}` | Actualizar usuario |
| DELETE | `/api/users/{id}` | Eliminar usuario (soft delete) |
| GET | `/api/users/activos` | Obtener usuarios activos |
| GET | `/api/users/buscar?q={term}` | Buscar usuarios |
| GET | `/api/users/paginated` | Obtener usuarios con paginación |

### Ejemplo de Request/Response

#### Crear Usuario
```bash
POST /api/users
Content-Type: application/json

{
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan.perez@email.com",
    "telefono": "123456789"
}
```

#### Response
```json
{
    "id": 1,
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan.perez@email.com",
    "telefono": "123456789",
    "nombreCompleto": "Juan Pérez",
    "fechaCreacion": "2024-01-15T10:30:00",
    "fechaActualizacion": "2024-01-15T10:30:00",
    "activo": true
}
```

### Estructura del Proyecto
```
src/
├── main/
│   ├── java/com/example/ejercicio/
│   │   ├── controller/      # Controladores REST
│   │   ├── service/         # Servicios de negocio
│   │   ├── repository/      # Repositorios de datos
│   │   ├── model/           # Entidades JPA
│   │   ├── dto/             # DTOs
│   │   ├── config/          # Configuración
│   │   └── exception/       # Manejo de excepciones
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/ejercicio/
        └── controller/      # Pruebas de controladores
```

### Datos de Ejemplo
Al iniciar la aplicación, se cargan automáticamente 5 usuarios de ejemplo:
- 4 usuarios activos
- 1 usuario inactivo (para demostrar soft delete)

## Manejo de Errores

La aplicación implementa un manejo global de errores que retorna respuestas consistentes:

```json
{
    "status": 404,
    "error": "Recurso no encontrado",
    "message": "Usuario no encontrado con ID: 999",
    "timestamp": "2024-01-15T10:30:00"
}
```

### Tipos de Errores Manejados
- **404 Not Found**: Recurso no encontrado
- **409 Conflict**: Conflicto (ej: email duplicado)
- **400 Bad Request**: Datos inválidos/validación fallida
- **500 Internal Server Error**: Errores internos

## Seguridad

La configuración de seguridad está diseñada para desarrollo y permite:
- Acceso libre a todos los endpoints de la API
- Acceso libre a la documentación Swagger
- Acceso libre a la consola H2
- Configuración CORS permisiva

## Validaciones Implementadas

### Usuario
- **Nombre**: Requerido, 2-50 caracteres
- **Apellido**: Requerido, 2-50 caracteres  
- **Email**: Requerido, formato válido, único
- **Teléfono**: Opcional, 10-15 caracteres

## Funcionalidades Destacadas

### 1. CRUD Completo
- Crear, leer, actualizar y eliminar usuarios
- Validación completa de datos
- Manejo de errores apropiado

### 2. Búsqueda Avanzada
- Búsqueda por nombre, apellido o email
- Búsqueda insensible a mayúsculas/minúsculas
- Resultados ordenados

### 3. Paginación
- Soporte completo para paginación
- Ordenamiento por cualquier campo
- Filtros por estado (activo/inactivo)

### 4. Soft Delete
- Eliminación lógica preservando datos
- Funcionalidad de activación/desactivación
- Consultas que respetan el estado
