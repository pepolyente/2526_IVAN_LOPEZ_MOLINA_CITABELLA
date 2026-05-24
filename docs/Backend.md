# Backend – Spring Boot 3.x API REST

## 1. Visión General

El backend es una API REST desarrollada con **Java 17**, **Spring Boot 3.x** y **Maven**.

Sigue una arquitectura por capas clara, con seguridad sin estado basada en JWT, validaciones robustas y manejo centralizado de errores.

Está diseñada para ser consumida por el frontend Angular y la aplicación Flutter.

- Nombre de la aplicación principal: `CitaBellaApiApplication`
- Paquete base: `com.citabella.citabellaapi`

---

# 2. Estructura de Paquetes

```plaintext
src/main/java/com/citabella/citabellaapi/
├── config/
│   ├── SecurityConfig.java
│   ├── JwtUtil.java
│   ├── JwtAuthFilter.java
│   ├── CorsConfig.java
│   ├── SwaggerConfig.java
│   └── demo/
│       └── DemoDataInitializer.java
├── entity/
│   ├── appointment/
│   ├── client/
│   ├── employee/
│   ├── product/
│   ├── sale/
│   ├── security/
│   ├── treatment/
│   ├── notification/
│   └── enums/
├── dto/
│   ├── appointment/
│   ├── auth/
│   ├── client/
│   ├── employee/
│   ├── page/
│   ├── product/
│   ├── sale/
│   ├── treatment/
│   └── user/
├── repository/
├── service/
│   ├── interfaces/
│   └── implementations/
├── controller/
│   ├── appointment/
│   ├── auth/
│   ├── client/
│   ├── employee/
│   ├── product/
│   └── treatment/
├── mappers/
├── exception/
└── docs/
```

## Configuración (`config/`)

| Archivo | Descripción |
|---|---|
| `SecurityConfig.java` | Cadena de filtros, sesión STATELESS, CSRF desactivado |
| `JwtUtil.java` | Generación y validación de JWT HS256 |
| `JwtAuthFilter.java` | Filtro JWT basado en `OncePerRequestFilter` |
| `CorsConfig.java` | Configuración global CORS |
| `SwaggerConfig.java` | OpenAPI 3 con `bearerAuth` |
| `DemoDataInitializer.java` | Datos demo automáticos |

---

# 3. Flujo de una Petición

El filtro `JwtAuthFilter` intercepta las peticiones protegidas.

## Header esperado

```http
Authorization: Bearer <token>
```

## Flujo interno

1. Extrae el token JWT
2. Valida el token usando `JwtUtil`
3. Extrae `username` y `role`
4. Construye un `UsernamePasswordAuthenticationToken`
5. Establece el `SecurityContextHolder`
6. Continúa la cadena de filtros

Los controladores reciben DTOs validados y devuelven DTOs usando mappers.

Las excepciones son capturadas por `GlobalExceptionHandler`.

---

# 4. Capas Principales

---

## 4.1 Controladores (REST)

Anotados con:

```java
@RestController
```

### Características

- Orquestación únicamente
- Sin lógica de negocio
- Uso de `@PreAuthorize`
- Respuestas paginadas mediante `PageResponse<T>`

---

## 4.2 Servicios

Cada dominio contiene:

- Interfaz
- Implementación
- `@Service`
- `@Transactional`

### Validaciones implementadas

- Nombre de tratamiento único
- Nombre de producto único
- Email único
- Username único
- Teléfono único
- Nombre de empleado único

### Vínculos de perfil

Un usuario solo puede estar asociado a:

- `NONE`
- `CLIENT`
- `EMPLOYEE`
- `ADMIN`

### Cambios automáticos

| Acción | Resultado |
|---|---|
| Vincular cliente | `CLIENT + ACTIVE` |
| Desvincular cliente | `USER + PENDING` |

### Solapamiento de citas

Consulta JPQL para detectar horarios concurrentes.

### Máquina de estados

```text
PENDING → CONFIRMED | CANCELLED
CONFIRMED → IN_PROGRESS | CANCELLED | NO_SHOW
IN_PROGRESS → COMPLETED | CANCELLED
```

### Borrado lógico

```java
active = false
```

Usuarios:

```java
accountStatus = LOCKED
```

---

## 4.3 Repositorios

Basados en:

```java
JpaRepository
```

### Ejemplos

```java
AppointmentRepository.hasOverlap(...)
AppointmentRepository.findAllByStatus(...)
UserRepository.findAllByAccountStatus(...)
ClientRepository.existsByPhoneNumber(...)
ProductRepository.findAllByActive(...)
TreatmentRepository.existsTreatmentByName(...)
```

---

## 4.4 DTOs

DTOs inmutables usando `record`.

### Validaciones Jakarta

```java
@NotBlank
@NotNull
@Email
@Size
@DecimalMin
@Min
@Positive
```

### Paginación

```java
PageResponse<T>
```

### Productos

#### Vista pública

```java
ProductPublicResponse
```

Campos:

- id
- name
- category
- salePrice
- imageKey

#### Vista privada

```java
ProductPrivateResponse
```

Incluye:

- purchasePrice
- supplier
- isCritical
- active

---

## 4.5 Mappers

Conversión Entity ↔ DTO.

### Métodos

```java
toResponse(Entity)

toDetailedResponse(Entity)

toResponseList(List<Entity>)

toPrivateResponse(Entity)

toPublicResponse(Entity)
```

---

# 5. API REST – Endpoints

---

# Autenticación (`/api/auth`)

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| POST | `/api/auth/login` | Público | Login + JWT |
| POST | `/api/auth/register` | Público | Registro |
| GET | `/api/auth/me` | Autenticado | Usuario actual |

---

# Citas (`/api/appointments`)

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/appointments` | ADMIN, EMPLOYEE | Listado |
| GET | `/api/appointments/my` | CLIENT | Mis citas |
| GET | `/api/appointments/{id}` | ADMIN, EMPLOYEE | Obtener |
| POST | `/api/appointments` | ADMIN, EMPLOYEE | Crear |
| PUT | `/api/appointments/update` | ADMIN, EMPLOYEE | Reagendar |
| PATCH | `/api/appointments/{id}/status` | ADMIN, EMPLOYEE | Cambiar estado |
| DELETE | `/api/appointments/{id}` | ADMIN, EMPLOYEE | Cancelar |

---

# Clientes (`/api/clients`)

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/clients` | ADMIN, EMPLOYEE | Listado |
| GET | `/api/clients/{id}` | ADMIN, EMPLOYEE | Obtener |
| POST | `/api/clients` | ADMIN, EMPLOYEE | Crear |
| PUT | `/api/clients/{id}` | ADMIN, EMPLOYEE | Actualizar |
| DELETE | `/api/clients/{id}` | ADMIN, EMPLOYEE | Desactivar |
| PATCH | `/api/clients/{clientId}/link-user/{userId}` | ADMIN, EMPLOYEE | Vincular usuario |
| PATCH | `/api/clients/{clientId}/unlink-user` | ADMIN, EMPLOYEE | Desvincular |

---

# Empleados (`/api/employees`)

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/employees` | ADMIN, EMPLOYEE | Listado |
| GET | `/api/employees/{id}` | ADMIN | Obtener |
| POST | `/api/employees` | ADMIN | Crear |
| PUT | `/api/employees/{id}` | ADMIN | Actualizar |
| DELETE | `/api/employees/{id}` | ADMIN | Desactivar |
| PATCH | `/api/employees/{id}/activate` | ADMIN | Reactivar |
| PATCH | `/api/employees/{employeeId}/link-user/{userId}` | ADMIN | Vincular usuario |

---

# Tratamientos (`/api/treatments`)

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/treatments` | Público | Listado |
| GET | `/api/treatments/{id}` | ADMIN, EMPLOYEE, CLIENT | Obtener |
| GET | `/api/treatments/detail` | ADMIN, EMPLOYEE | Detallado |
| POST | `/api/treatments` | ADMIN | Crear |
| PUT | `/api/treatments/{id}` | ADMIN | Actualizar |
| DELETE | `/api/treatments/{id}` | ADMIN | Desactivar |
| PATCH | `/api/treatments/{id}/activate` | ADMIN | Reactivar |

---

# Productos (`/api/products`)

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/products` | Público | Vista pública |
| GET | `/api/products/{id}` | ADMIN, EMPLOYEE | Vista privada |
| GET | `/api/products/admin` | ADMIN, EMPLOYEE | Listado privado |
| POST | `/api/products` | ADMIN | Crear |
| PUT | `/api/products/{id}` | ADMIN | Actualizar |
| DELETE | `/api/products/{id}` | ADMIN | Desactivar |
| PATCH | `/api/products/{id}/activate` | ADMIN | Reactivar |

---

# Usuarios (`/api/users`)

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/users` | ADMIN | Listado |
| GET | `/api/users/{id}` | ADMIN | Obtener |
| POST | `/api/users` | ADMIN | Crear |
| PUT | `/api/users/{id}` | ADMIN | Actualizar |
| DELETE | `/api/users/{id}` | ADMIN | Desactivar |
| PATCH | `/api/users/{id}/activate` | ADMIN | Activar |
| PATCH | `/api/users/{id}/swap-role/{name}` | ADMIN | Cambiar rol |

---

# Roles (`/api/roles`)

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/roles/{name}` | ADMIN | Obtener rol |

---

# Swagger

```text
/swagger-ui.html
/v3/api-docs
```

---

# 6. Seguridad

---

## JwtAuthFilter

### Funciones

- Extraer JWT
- Validar JWT
- Crear contexto de seguridad

### Configuración

```properties
jwt.secret=
jwt.expiration=
```

### Algoritmo

```text
HS256
```

---

## SecurityConfig

### Rutas públicas

```text
/api/auth/login
/api/auth/register
/api/products
/api/treatments
/swagger-ui/**
/v3/api-docs/**
/actuator/health
```

### Configuración

- STATELESS
- CSRF desactivado
- HTTP Basic desactivado
- Form Login desactivado

### UserDetailsService

Carga usuarios desde:

```java
UserRepository
```

---

## CorsConfig

```text
Origins: *
Methods: GET, POST, PUT, DELETE, OPTIONS
Headers: *
```

---

## Contraseñas

```java
BCryptPasswordEncoder
```

---

## Roles

| Rol | Descripción |
|---|---|
| ADMIN | Administrador |
| EMPLOYEE | Empleado |
| CLIENT | Cliente |
| USER | Usuario base |

---

## Autorización

```java
@PreAuthorize("hasRole('ADMIN')")

@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")

@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CLIENT')")
```

---

# 7. Manejo Global de Excepciones

## ErrorResponse

```java
record ErrorResponse(
    int status,
    String message,
    LocalDateTime timestamp
)
```

## Excepciones

| Excepción | Código | Comportamiento |
|---|---|---|
| ResourceNotFoundException | 404 | Recurso no encontrado |
| BadRequestException | 400 | Petición inválida |
| MethodArgumentNotValidException | 400 | Error validación |
| DataIntegrityViolationException | 400 | Invalid data provided |
| AccessDeniedException | 403 | Sin permisos |
| Exception | 500 | Internal Server Error |

---

# 8. Inicialización de Datos

`DemoDataInitializer` se ejecuta si:

```properties
app.demo.enabled=true
```

y la base de datos está vacía.

## Datos creados

### Roles

- ADMIN
- EMPLOYEE
- CLIENT
- USER

### Administrador

```properties
admin.username=citabella
admin.email=admin@citabella.com
admin.password=citabella123
```

### Datos demo

- 10 empleados
- 45 clientes
- 6 usuarios
- 16 tratamientos
- 20 productos

---

# 9. Modelo de Dominio

---

## User

Campos:

- id
- username
- email
- passwordHash
- profileType
- accountStatus
- role

---

## Client

Campos:

- id
- name
- phoneNumber
- birthday
- gender
- active

---

## Employee

Campos:

- id
- name
- position
- commission
- active

---

## Treatment

Campos:

- id
- name
- description
- minimumDuration
- maximumDuration
- price
- active

---

## Appointment

Campos:

- id
- startAt
- endAt
- status
- notes
- hasOverlap

---

## Product

Campos:

- id
- name
- category
- purchasePrice
- salePrice
- usageType
- supplier
- isCritical
- active
- imageKey

---

## Sale

Campos:

- id
- soldAt
- totalAmount
- paymentMethod
- state

---

# 10. Enums del Dominio

| Enum | Valores |
|---|---|
| AccountStatus | PENDING, ACTIVE, LOCKED |
| ProfileType | NONE, CLIENT, EMPLOYEE, ADMIN |
| AppointmentStatus | PENDING, CONFIRMED, IN_PROGRESS, CANCELLED, COMPLETED, NO_SHOW |
| SaleState | PENDING, PAID, COMPLETED, CANCELLED, REFUNDED |
| PaymentMethod | CASH, CARD, TRANSFER, OTHER |
| UsageType | INTERNAL, SALE, BOTH |
| MovementType | INBOUND, OUTBOUND, ADJUSTMENT |
| ReferenceType | SALE, APPOINTMENT, ADJUSTMENT, OTHER |
| NotificationType | REMINDER, BIRTHDAY, PROMOTION, CANCELLATION, CONFIRMATION |
| NotificationChannel | EMAIL, PUSH, WHATSAPP |
| NotificationStatus | PENDING, SENT, FAILED |
| DeviceType | ANDROID, IOS, WEB |
| RequestStatus | PENDING, ACCEPTED, REJECTED |
| RequestChannel | WEB, WHATSAPP, APP |
| RequestType | CREATE_APPOINTMENT, UPDATE_APPOINTMENT, CANCEL_APPOINTMENT |
| AuditAction | INSERT, UPDATE, DELETE |
| Gender | MALE, FEMALE, OTHER |

---

# 11. Variables de Entorno

## Perfil Docker

| Variable | Descripción | Ejemplo |
|---|---|---|
| SPRING_DATASOURCE_URL | URL MySQL | jdbc:mysql://db:3306/citabella_db |
| SPRING_DATASOURCE_USERNAME | Usuario DB | citabella |
| SPRING_DATASOURCE_PASSWORD | Password DB | citabella123 |
| JWT_SECRET | Clave JWT | mi_clave_super_segura |
| JWT_EXPIRATION | Expiración JWT | 86400000 |
| ADMIN_USERNAME | Usuario admin | citabella |
| ADMIN_EMAIL | Email admin | admin@citabella.com |
| ADMIN_PASSWORD | Password admin | citabella123 |

## Configuración demo

```properties
app.demo.enabled=true
```

---

# 12. Servicios Pendientes

| Dominio | Estado |
|---|---|
| Ventas | Repositorios listos |
| Stock | Repositorios listos |
| Notificaciones | Repositorios listos |
| Auditoría | Repositorio listo |
| Submissions | Servicio vacío |
| EmployeeTreatment | Servicio implementado sin controller |

---

# 13. Buenas Prácticas

- Arquitectura por capas
- DTOs inmutables
- Jakarta Validation
- Seguridad JWT stateless
- Borrado lógico
- Paginación uniforme
- Swagger/OpenAPI
- Mappers estáticos
- Manejo global de excepciones
- Datos demo automáticos

---

# 14. Escalabilidad

## Funcionalidades futuras

- Ventas
- Stock
- Notificaciones
- Auditoría
- Submissions

## Seguridad avanzada

- JWT RS256
- Rotación de claves
- RefreshToken

## Producción

- Restringir CORS

## Arquitectura

- Migración a microservicios

## Auditoría

Implementar:

```java
AuditLogServiceImpl
```