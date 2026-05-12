# Backend – Spring Boot 3.x API REST

## 1. Visión General

El backend es una API REST desarrollada con **Java 17**, **Spring Boot 3.x** y **Maven**.  
Sigue una arquitectura por capas clara, con seguridad sin estado basada en JWT, validaciones robustas y manejo
centralizado de errores.

Está diseñada para ser consumida por el frontend Angular y la aplicación Flutter.

Nombre de la aplicación principal: `CitaBellaApiApplication`  
Paquete base: `com.citabella.citabellaapi`

---

## 2. Estructura de Paquetes

```plaintext
src/main/java/com/citabella/citabellaapi/
├── config/
│   ├── SecurityConfig.java         # Cadena de filtros, sesión STATELESS, CSRF desactivado
│   ├── JwtUtil.java                # Generación y validación de tokens HS256
│   ├── JwtAuthFilter.java          # Filtro OncePerRequestFilter
│   ├── CorsConfig.java             # CORS global (origins: *, métodos: GET/POST/PUT/DELETE/OPTIONS)
│   ├── SwaggerConfig.java          # OpenAPI 3 con esquema bearerAuth
│   └── PasswordEncoderConfig.java
├── entity/
│   ├── appointment/                # Appointment, AppointmentSubmission
│   ├── client/                     # Client, DeviceToken
│   ├── employee/                   # Employee, EmployeeTreatment, EmployeeTreatmentId
│   ├── product/                    # Product, Stock, StockMovement, Warehouse
│   ├── sale/                       # Sale, SaleProductDetail, SaleTreatmentDetail
│   ├── security/                   # User, Role, RefreshToken, AuditLog
│   ├── treatment/                  # Treatment
│   ├── notification/               # Notification
│   └── enums/                      # AccountStatus, AppointmentStatus, ProfileType,
│                                   # UsageType, PaymentMethod, SaleState, Gender,
│                                   # MovementType, ReferenceType, DeviceType,
│                                   # NotificationType, NotificationStatus,
│                                   # NotificationChannel, RequestStatus,
│                                   # RequestChannel, RequestType, AuditAction
├── dto/
│   ├── appointment/
│   ├── auth/                       # LoginRequest/Response, RegisterRequest,
│   │                               # UserInfoResponse, JwtResponse, RefreshTokenRequest
│   ├── client/
│   ├── employee/
│   ├── page/                       # PageResponse<T> (wrapper de paginación)
│   ├── product/                    # ProductRequest, ProductPublicResponse,
│   │                               # ProductPrivateResponse
│   ├── sale/
│   ├── treatment/                  # TreatmentRequest, TreatmentResponse,
│   │                               # TreatmentDetailedResponse
│   └── user/                       # UserRequest, UserResponse, UserUpdateRequest,
│                                   # RoleResponse
├── repository/                     # Interfaces JPA con consultas JPQL personalizadas
├── service/
│   ├── interfaces/                 # Definición de contratos de servicio
│   └── implementations/            # Lógica de negocio + DataInitializer
├── controller/
│   ├── appointment/
│   ├── auth/                       # AuthController, UserController, RoleController
│   ├── client/
│   ├── employee/
│   ├── product/
│   └── treatment/
├── mappers/                        # Convertidores estáticos Entity ↔ DTO
├── exception/                      # GlobalExceptionHandler, ErrorResponse,
│                                   # BadRequestException, ResourceNotFoundException
└── docs/
    └── ApiSecurityDocs.java        # Constantes de descripción para Swagger
```

---

## 3. Flujo de una Petición (Request/Response)

El filtro `JwtAuthFilter` intercepta todas las peticiones protegidas, extrae el token del encabezado:

```http
Authorization: Bearer <token>
```

Luego:

1. Valida el token con `JwtUtil`.
2. Extrae `username` y `role` del payload del token.
3. Construye un `UsernamePasswordAuthenticationToken` con la autoridad `ROLE_<role>`.
4. Establece el contexto de seguridad (`SecurityContextHolder`).
5. Permite continuar la petición por la cadena de filtros.

Los controladores reciben DTOs validados y devuelven DTOs gracias a los mappers. Las excepciones son capturadas por
`GlobalExceptionHandler` y transformadas en respuestas estructuradas.

---

## 4. Capas Principales

### 4.1 Controladores (REST)

Anotados con `@RestController`, exponen los endpoints definidos en la API.

Características:

- Solo realizan orquestación.
- No contienen lógica de negocio.
- Los métodos protegidos utilizan `@PreAuthorize`.
- Todos los listados devuelven `PageResponse<T>` (paginado vía Spring Data `Pageable`).

---

### 4.2 Servicios

Cada dominio dispone de una interfaz y su implementación (`@Service` + `@Transactional`).

La lógica de negocio incluye:

- Validaciones de unicidad:
    - nombre de tratamiento
    - nombre de producto
    - email y username de usuario
    - número de teléfono de cliente
    - nombre de empleado

- Verificación de vínculos de perfil:
    - un usuario solo puede estar vinculado a un cliente o un empleado (`ProfileType`: `NONE`, `CLIENT`, `EMPLOYEE`)
    - al vincular un cliente a un usuario, su rol pasa a `CLIENT` y su `AccountStatus` a `ACTIVE`
    - al desvincular, el rol vuelve a `USER` y el estado a `PENDING`

- Detección de solapamientos de citas:
    - consulta JPQL que comprueba franjas horarias concurrentes para el mismo empleado

- Máquina de estados de citas:
    - `PENDING` → `CONFIRMED` | `CANCELLED`
    - `CONFIRMED` → `IN_PROGRESS` | `CANCELLED` | `NO_SHOW`
    - `IN_PROGRESS` → `COMPLETED` | `CANCELLED`

- Borrado lógico:
    - la mayoría de entidades (Client, Employee, Treatment, Product) usan `active = false` en lugar de borrado físico
    - los usuarios desactivados pasan a `accountStatus = LOCKED`

---

### 4.3 Repositorios

Basados en `JpaRepository`, con consultas personalizadas en JPQL cuando es necesario.

Ejemplos destacados:

- `AppointmentRepository.hasOverlap(id, startAt, endAt)` — detecta solapamientos mediante JPQL
- `AppointmentRepository.findAllByStatus(status, pageable)`
- `UserRepository.findAllByAccountStatus(accountStatus, pageable)`
- `ClientRepository.existsByPhoneNumber(...)` / `existsByUser_Id(...)`
- `ProductRepository.findAllByActive(active, pageable)`
- `TreatmentRepository.existsTreatmentByName(name)`

Spring Data genera la implementación automáticamente.

---

### 4.4 DTOs (Records)

Objetos inmutables (Java `record`) con validaciones de Jakarta:

```java
@NotBlank
@NotNull
@Email
@Size
@DecimalMin
@Min /@Positive
```

Los controladores utilizan `@Valid` para validar automáticamente los datos antes de llamar al servicio.

Los listados devuelven `PageResponse<T>`, un wrapper que encapsula el `Page<T>` de Spring Data con información de
paginación (contenido, total de elementos, total de páginas, etc.).

Existen dos vistas de Producto:

- `ProductPublicResponse` — expone solo `id`, `name`, `category`, `salePrice`, `imageKey`
- `ProductPrivateResponse` — expone además `purchasePrice`, `supplier`, `isCritical`, `active`

---

### 4.5 Mappers

Clases estáticas en el paquete `mappers` con métodos como:

```java
toResponse(Entity)

toDetailedResponse(Entity)

toResponseList(List<Entity>)

toPrivateResponse(Entity)   // Products

toPublicResponse(Entity)    // Products
```

Centralizan la conversión y mantienen el código limpio sin dependencias externas de mapping.

---

## 5. API REST – Endpoints Completos

### Autenticación (`/api/auth`)

| Método | Endpoint             | Acceso      | Descripción                                              |
|--------|----------------------|-------------|----------------------------------------------------------|
| POST   | `/api/auth/login`    | Público     | Login; devuelve JWT + username + role                    |
| POST   | `/api/auth/register` | Público     | Registro; crea usuario con rol `USER` y estado `PENDING` |
| GET    | `/api/auth/me`       | Autenticado | Devuelve id, username, email y role del usuario actual   |

### Citas (`/api/appointments`)

| Método | Endpoint                        | Acceso          | Descripción                                  |
|--------|---------------------------------|-----------------|----------------------------------------------|
| GET    | `/api/appointments`             | ADMIN, EMPLOYEE | Listado paginado, filtrable por `status`     |
| GET    | `/api/appointments/{id}`        | ADMIN, EMPLOYEE | Obtener cita por ID                          |
| POST   | `/api/appointments`             | ADMIN, EMPLOYEE | Crear cita (estado inicial: `PENDING`)       |
| PUT    | `/api/appointments/update`      | ADMIN, EMPLOYEE | Reagendar cita                               |
| PATCH  | `/api/appointments/{id}/status` | ADMIN, EMPLOYEE | Cambiar estado según máquina de transiciones |
| DELETE | `/api/appointments/{id}`        | ADMIN, EMPLOYEE | Cancelar cita (borrado lógico)               |

### Clientes (`/api/clients`)

| Método | Endpoint                                     | Acceso          | Descripción                              |
|--------|----------------------------------------------|-----------------|------------------------------------------|
| GET    | `/api/clients`                               | ADMIN, EMPLOYEE | Listado paginado, filtrable por `active` |
| GET    | `/api/clients/{id}`                          | ADMIN, EMPLOYEE | Obtener cliente por ID                   |
| POST   | `/api/clients`                               | ADMIN, EMPLOYEE | Crear cliente                            |
| PUT    | `/api/clients/{id}`                          | ADMIN, EMPLOYEE | Actualizar cliente                       |
| DELETE | `/api/clients/{id}`                          | ADMIN, EMPLOYEE | Desactivar cliente (borrado lógico)      |
| PATCH  | `/api/clients/{clientId}/link-user/{userId}` | ADMIN, EMPLOYEE | Vincular usuario a cliente               |
| PATCH  | `/api/clients/{clientId}/unlink-user`        | ADMIN, EMPLOYEE | Desvincular usuario de cliente           |

### Empleados (`/api/employees`)

| Método | Endpoint                                         | Acceso | Descripción                              |
|--------|--------------------------------------------------|--------|------------------------------------------|
| GET    | `/api/employees`                                 | ADMIN  | Listado paginado, filtrable por `active` |
| GET    | `/api/employees/{id}`                            | ADMIN  | Obtener empleado por ID                  |
| POST   | `/api/employees`                                 | ADMIN  | Crear empleado                           |
| PUT    | `/api/employees/{id}`                            | ADMIN  | Actualizar empleado                      |
| DELETE | `/api/employees/{id}`                            | ADMIN  | Desactivar empleado (borrado lógico)     |
| PATCH  | `/api/employees/{id}/activate`                   | ADMIN  | Reactivar empleado                       |
| PATCH  | `/api/employees/{employeeId}/link-user/{userId}` | ADMIN  | Vincular usuario a empleado              |

### Tratamientos (`/api/treatments`)

| Método | Endpoint                 | Acceso                  | Descripción                                        |
|--------|--------------------------|-------------------------|----------------------------------------------------|
| GET    | `/api/treatments`        | Público                 | Listado paginado de tratamientos activos           |
| GET    | `/api/treatments/{id}`   | ADMIN, EMPLOYEE, CLIENT | Obtener tratamiento por ID                         |
| GET    | `/api/treatments/detail` | ADMIN                   | Listado detallado paginado, filtrable por `active` |
| POST   | `/api/treatments`        | ADMIN                   | Crear tratamiento                                  |
| PUT    | `/api/treatments/{id}`   | ADMIN                   | Actualizar tratamiento                             |
| DELETE | `/api/treatments/{id}`   | ADMIN                   | Desactivar tratamiento (borrado lógico)            |

### Productos (`/api/products`)

| Método | Endpoint              | Acceso          | Descripción                                              |
|--------|-----------------------|-----------------|----------------------------------------------------------|
| GET    | `/api/products`       | Público         | Listado de productos activos (vista pública)             |
| GET    | `/api/products/{id}`  | ADMIN, EMPLOYEE | Obtener producto por ID (vista privada)                  |
| GET    | `/api/products/admin` | ADMIN, EMPLOYEE | Listado paginado, filtrable por `active` (vista privada) |
| POST   | `/api/products`       | ADMIN           | Crear producto                                           |
| PUT    | `/api/products/{id}`  | ADMIN           | Actualizar producto                                      |
| DELETE | `/api/products/{id}`  | ADMIN           | Desactivar producto (borrado lógico)                     |

### Usuarios (`/api/users`)

| Método | Endpoint                           | Acceso | Descripción                                     |
|--------|------------------------------------|--------|-------------------------------------------------|
| GET    | `/api/users`                       | ADMIN  | Listado paginado, filtrable por `accountStatus` |
| GET    | `/api/users/{id}`                  | ADMIN  | Obtener usuario por ID                          |
| POST   | `/api/users`                       | ADMIN  | Crear usuario                                   |
| PUT    | `/api/users/{id}`                  | ADMIN  | Actualizar usuario                              |
| DELETE | `/api/users/{id}`                  | ADMIN  | Desactivar usuario (`accountStatus → LOCKED`)   |
| PATCH  | `/api/users/{id}/swap-role/{name}` | ADMIN  | Cambiar rol de un usuario                       |

### Roles (`/api/roles`)

| Método | Endpoint            | Acceso | Descripción            |
|--------|---------------------|--------|------------------------|
| GET    | `/api/roles/{name}` | ADMIN  | Obtener rol por nombre |

La documentación interactiva completa está disponible en:

```
/swagger-ui.html
/v3/api-docs
```

---

## 6. Seguridad

### JwtAuthFilter

Filtro personalizado que:

- Extiende `OncePerRequestFilter`
- Extrae el token del header `Authorization: Bearer <token>`
- Valida el token con `JwtUtil`
- Extrae `username` y `role` del payload
- Construye el `SecurityContext` con autoridad `ROLE_<role>`

Configurado en `jwt.secret` y `jwt.expiration` (vía `application.properties`).

Algoritmo de firma: **HS256**

---

### SecurityConfig

Define:

- Rutas públicas: `/api/auth/login`, `/api/auth/register`, `GET /api/products`, `GET /api/treatments`, `/swagger-ui/**`,
  `/v3/api-docs/**`
- Sesión `STATELESS`
- CSRF desactivado
- HTTP Basic y Form Login desactivados
- Inclusión del filtro `JwtAuthFilter` antes de `UsernamePasswordAuthenticationFilter`
- `UserDetailsService` personalizado que carga usuarios desde `UserRepository`

---

### CorsConfig

Configuración global permisiva (orientada a desarrollo):

- Orígenes: `*`
- Métodos permitidos: `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`
- Headers: `*`

---

### Contraseñas

Codificación mediante `BCryptPasswordEncoder` (definido como `@Bean` en `SecurityConfig`).

---

### Roles y ciclo de vida de usuario

Roles disponibles (inicializados por `DataInitializer`):

| Rol        | Descripción                             |
|------------|-----------------------------------------|
| `ADMIN`    | Administrador del sistema               |
| `EMPLOYEE` | Empleado de la empresa                  |
| `CLIENT`   | Cliente verificado con perfil vinculado |
| `USER`     | Usuario registrado sin perfil asignado  |

Al registrarse (`/api/auth/register`), el usuario recibe el rol `USER` y `accountStatus = PENDING`. El perfil se activa
cuando un ADMIN o EMPLOYEE vincula la cuenta a un Client o Employee, momento en que el rol y estado se actualizan
automáticamente.

Autorización a nivel de método mediante:

```java
@PreAuthorize("hasRole('ADMIN')")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CLIENT')")
```

---

## 7. Manejo Global de Excepciones

La clase `GlobalExceptionHandler` (anotada con `@RestControllerAdvice`) captura excepciones y retorna respuestas
uniformes mediante el record `ErrorResponse`:

```java
record ErrorResponse(int status, String message, LocalDateTime timestamp)
```

### Excepciones gestionadas

| Excepción                         | Código HTTP | Comportamiento                                         |
|-----------------------------------|-------------|--------------------------------------------------------|
| `ResourceNotFoundException`       | 404         | Mensaje descriptivo del recurso no encontrado          |
| `BadRequestException`             | 400         | Mensaje descriptivo de la petición inválida            |
| `MethodArgumentNotValidException` | 400         | Primer error de campo de la validación Jakarta         |
| `DataIntegrityViolationException` | 400         | Mensaje genérico: `"Invalid data provided"`            |
| `AccessDeniedException`           | 403         | Mensaje genérico: `"You do not have permission..."`    |
| `Exception` (general)             | 500         | Mensaje: `"Internal Server Error: " + ex.getMessage()` |

> ⚠️ **Nota**: `DataIntegrityViolationException` retorna **400**, no 409.

Esto evita fugas de información interna y facilita el manejo en el cliente.

---

## 8. Inicialización de Datos

`DataInitializer` es un `@Component` en `service/implementations/` que se ejecuta al arrancar la aplicación mediante
`@PostConstruct`.

Datos creados al inicio (solo si no existen):

**Roles**: `ADMIN`, `EMPLOYEE`, `USER`, `CLIENT`

**Usuarios de prueba**:

| Username      | Email                  | Rol    |
|---------------|------------------------|--------|
| `Admin`       | admin@citabella.com    | ADMIN  |
| `Desconocido` | desconocido@opa.com    | USER   |
| `Cliente`     | cliente@buenagente.com | CLIENT |

**Clientes**: Ivan (vinculado al usuario `Cliente`), Angela

**Empleados**: Ruth Molina (Jefa), Ivan L (Barredor)

**Tratamientos**: "Lavar cabeza", "Cortar puntas"

**Productos**: "SuavePelo" (Mascarilla, `UsageType.BOTH`), "CortaPelo" (Tijeras, `UsageType.INTERNAL`)

Características: idempotente, sin scripts SQL manuales.

---

## 9. Modelo de Dominio – Entidades Principales

### User

Campos: `id`, `username` (único), `email` (único), `passwordHash`, `profileType` (`NONE`/`CLIENT`/`EMPLOYEE`),
`accountStatus` (`PENDING`/`ACTIVE`/`LOCKED`), `createdAt`, `role`, `client` (OneToOne), `employee` (OneToOne).

### Client

Campos: `id`, `name`, `phoneNumber` (único), `birthday`, `gender`, `active`, `user` (OneToOne), `appointments`, `sales`.

### Employee

Campos: `id`, `name` (único), `position`, `commission` (default `0`), `active`, `user` (OneToOne), `appointments`,
`sales`.

### Treatment

Campos: `id`, `name` (único), `description`, `minimumDuration`, `maximumDuration` (opcional), `price`, `active`.
Relación ManyToMany con `Appointment`.

### Appointment

Campos: `id`, `startAt`, `endAt`, `status` (default `PENDING`), `notes`, `hasOverlap`, `client`, `employee`,
tratamientos (ManyToMany), `submissions`.

### Product

Campos: `id`, `name`, `category`, `purchasePrice`, `salePrice`, `usageType` (default `BOTH`), `supplier`, `isCritical` (
default `false`), `active`, `imageKey`.

### Sale

Campos: `id`, `soldAt`, `totalAmount`, `paymentMethod` (default `CASH`), `state`, `client`, `employee`, `appointment` (
OneToOne opcional).

---

## 10. Enums del Dominio

| Enum                  | Valores                                                                    |
|-----------------------|----------------------------------------------------------------------------|
| `AccountStatus`       | `PENDING`, `ACTIVE`, `LOCKED`                                              |
| `ProfileType`         | `NONE`, `CLIENT`, `EMPLOYEE`                                               |
| `AppointmentStatus`   | `PENDING`, `CONFIRMED`, `IN_PROGRESS`, `CANCELLED`, `COMPLETED`, `NO_SHOW` |
| `SaleState`           | `PENDING`, `PAID`, `COMPLETED`, `CANCELLED`, `REFUNDED`                    |
| `PaymentMethod`       | `CASH`, `CARD`, `TRANSFER`, `OTHER`                                        |
| `UsageType`           | `INTERNAL`, `SALE`, `BOTH`                                                 |
| `MovementType`        | `INBOUND`, `OUTBOUND`, `ADJUSTMENT`                                        |
| `ReferenceType`       | `SALE`, `APPOINTMENT`, `ADJUSTMENT`, `OTHER`                               |
| `NotificationType`    | `REMINDER`, `BIRTHDAY`, `PROMOTION`, `CANCELLATION`, `CONFIRMATION`        |
| `NotificationChannel` | `EMAIL`, `PUSH`, `WHATSAPP`                                                |
| `NotificationStatus`  | `PENDING`, `SENT`, `FAILED`                                                |
| `DeviceType`          | `ANDROID`, `IOS`, `WEB`                                                    |
| `RequestStatus`       | `PENDING`, `ACCEPTED`, `REJECTED`                                          |
| `RequestChannel`      | `WEB`, `WHATSAPP`, `APP`                                                   |
| `RequestType`         | `CREATE_APPOINTMENT`, `UPDATE_APPOINTMENT`, `CANCEL_APPOINTMENT`           |
| `AuditAction`         | `INSERT`, `UPDATE`, `DELETE`                                               |
| `Gender`              | `MALE`, `FEMALE` (y otros valores del enum)                                |

---

## 11. Buenas Prácticas y Patrones

- Separación interfaz-implementación en servicios (`@Service` + `@Transactional`)
- Uso de `record` de Java para DTOs inmutables
- Validaciones declarativas (Jakarta Validation) y programáticas en servicios
- Manejo centralizado de excepciones (`@RestControllerAdvice`)
- Seguridad stateless con JWT (sin sesión HTTP)
- Borrado lógico en lugar de físico en entidades de negocio
- Paginación uniforme con `PageResponse<T>` en todos los listados
- Documentación automática con OpenAPI 3 / Swagger, incluyendo esquema `bearerAuth`
- Mappers estáticos sin dependencias externas (sin MapStruct ni ModelMapper)
- Constantes de documentación Swagger centralizadas en `ApiSecurityDocs`

---

## 12. Dominios Scaffolded (Fase Futura)

Las siguientes áreas tienen entidades y repositorios definidos pero sus servicios y controladores están vacíos —
pendientes de implementar en fases posteriores:

| Dominio        | Entidades                                          | Estado                               |
|----------------|----------------------------------------------------|--------------------------------------|
| Ventas         | `Sale`, `SaleProductDetail`, `SaleTreatmentDetail` | Repositorios listos, servicio vacío  |
| Stock          | `Stock`, `StockMovement`, `Warehouse`              | Repositorios listos, servicio vacío  |
| Notificaciones | `Notification`, `DeviceToken`                      | Repositorios listos, servicio vacío  |
| Auditoría      | `AuditLog`                                         | Repositorio listo, servicio vacío    |
| Submissions    | `AppointmentSubmission`                            | Repositorio listo, controlador vacío |

---

## 13. Posibles Puntos de Escalabilidad

**Nuevas funcionalidades**: Implementar los controladores y servicios ya scaffolded para ventas, stock, notificaciones y
auditoría utilizando los repositorios existentes.

**Seguridad avanzada**: Mejorar JWT con claves asimétricas `RS256`, rotación de claves y activar el `RefreshToken` (
entidad y DTO ya definidos pero sin implementar).

**CORS en producción**: Restringir los `allowedOrigins` a dominios específicos en lugar de `*`.

**Microservicios**: La separación clara de dominios facilita una eventual migración a arquitectura de microservicios.

**Auditoría**: `AuditLog` ya tiene entidad con campos `affectedTable`, `affectedId`, `action` (`AuditAction`),
`occurredAt`, `jsonDetail` y `user`. Solo requiere implementar `AuditLogServiceImpl`.