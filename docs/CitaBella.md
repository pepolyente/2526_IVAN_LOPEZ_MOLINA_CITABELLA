# CitaBella – Sistema Integral de Gestión para Peluquerías

**Autor:** Ivan López Molina  
**Ciclo:** 2º DAM – IES La Vereda  
**Curso:** 2025 – 2026  
**Tutor:** Francisco Hernández

---

# 1. Propósito del Proyecto

CitaBella es un sistema digital creado para reemplazar la gestión manual (agenda en papel y comunicación por WhatsApp)
de la peluquería real **“Ruth Molina”**.

Centraliza:

- citas
- clientes
- empleados
- tratamientos
- productos

En una única plataforma web segura con control de acceso por roles, preparada para crecer con módulos de:

- inventario
- ventas
- notificaciones

---

# 2. Arquitectura General

El sistema sigue una arquitectura cliente-servidor multicapa con separación estricta entre:

- frontend
- backend
- base de datos

Todos los componentes están orquestados mediante contenedores Docker.

## Componentes

### Frontend Web

- Angular v18
- Lazy loading
- Zona pública y panel privado

### Backend

- Spring Boot 3.x
- Java 21
- API REST documentada con Swagger/OpenAPI

### Base de Datos

- MySQL 8
- Esquema relacional normalizado
- Acceso mediante JPA/Hibernate

### Proxy inverso / Servidor web

- Nginx
- Sirve los estáticos compilados de Angular
- Redirige `/api` al backend

### Orquestación

- Docker Compose
- Servicios:
    - `db`
    - `api`
    - `web`

Con volúmenes para persistencia.

---

# 3. Tecnologías Detectadas

| Capa          | Tecnología                                                                           |
|---------------|--------------------------------------------------------------------------------------|
| Backend       | Java 21, Spring Boot 3.x, Spring Security, Spring Data JPA, Springdoc OpenAPI, Maven |
| Base de datos | MySQL 8, Hibernate, JPA                                                              |
| Frontend Web  | Angular 18 (NgModule), TypeScript, RxJS, Angular Signals, CSS Tokens                 |
| Móvil         | Flutter (contenedor WebView)                                                         |
| Proxy / Web   | Nginx                                                                                |
| Contenedores  | Docker, Docker Compose                                                               |
| Seguridad     | JWT (HS256), BCrypt, roles (ADMIN, EMPLOYEE, CLIENT)                                 |
| Herramientas  | IntelliJ IDEA, Git/GitHub, GitHub Projects, Postman, WSL2                            |

---

# 4. Flujo Frontend ↔ Backend ↔ Base de Datos

## Flujo general

1. El usuario accede mediante:
    - navegador web (Angular)
    - aplicación Flutter (WebView)

2. La zona pública muestra:
    - servicios
    - productos

Sin autenticación.

3. Para acceder al panel privado:
    - el usuario inicia sesión en `/login`
    - el backend valida credenciales
    - genera un token JWT
    - devuelve el token al frontend

4. El frontend:
    - almacena el token en `localStorage`
    - lo adjunta automáticamente en cada petición

```http
Authorization: Bearer <token>
```

Mediante un interceptor HTTP de Angular.

5. Nginx:
    - redirige `/api/*` al backend
    - sirve Angular como archivos estáticos
    - usa fallback a `index.html`

6. El backend:
    - valida JWT con `JwtAuthFilter`
    - ejecuta lógica de negocio
    - accede a MySQL mediante JPA

7. Las respuestas JSON vuelven al frontend y la UI se actualiza reactivamente.

---

# 5. Mecanismo de Autenticación y Autorización

## Autenticación

Sistema stateless con JWT.

El endpoint:

```plaintext
/api/auth/login
```

Recibe credenciales y:

- verifica contraseña con BCrypt
- genera token firmado HS256
- incluye usuario y rol
- aplica expiración configurable

---

## Autorización

Protección mediante:

- `SecurityFilterChain`
- `@PreAuthorize`

Roles disponibles:

- `ADMIN`
- `EMPLOYEE`
- `CLIENT`

---

## Cliente Angular

### AuthInterceptor

Inyecta automáticamente el JWT.

### Route Guards

Protección de rutas mediante:

- `AuthGuard`
- `RoleGuard`

---

# 6. Estructura Modular del Sistema

# Frontend Angular (NgModule + Lazy Loading)

| Módulo             | Contenido principal              | Acceso requerido |
|--------------------|----------------------------------|------------------|
| PublicModule       | Home, ServicesPage, ProductsPage | Público          |
| AuthModule         | Login, Register                  | Público          |
| AppointmentsModule | AppointmentList, AppointmentForm | ADMIN, EMPLOYEE  |
| ClientsModule      | ClientList, ClientForm           | ADMIN, EMPLOYEE  |
| EmployeesModule    | EmployeeList                     | ADMIN            |
| TreatmentsModule   | TreatmentList, TreatmentForm     | ADMIN            |
| AdminModule        | UserList                         | ADMIN            |

## CoreModule

Registra servicios globales:

- interceptor de autenticación
- servicios compartidos

---

## ThemeService

Gestiona:

- tema claro
- tema oscuro

Usando señales reactivas de Angular.

---

## Servicios Angular

Encapsulan llamadas HTTP y retornan:

```typescript
Observable<T>
```

---

# Backend (Arquitectura por capas)

| Paquete    | Responsabilidad                        |
|------------|----------------------------------------|
| entity     | Clases JPA, relaciones y enumeraciones |
| repository | Interfaces JPA y consultas JPQL        |
| service    | Lógica de negocio                      |
| controller | Endpoints REST                         |
| dto        | Records Java con validaciones          |
| mapper     | Conversión entidad ↔ DTO               |
| config     | Seguridad, JWT, CORS, Swagger          |
| exception  | Manejo global de excepciones           |

---

# 7. Integración Docker / Nginx

El archivo `docker-compose.yml` define:

- tres servicios
- una red interna

## Servicios

### citabella-db

- MySQL 8
- volumen persistente

### citabella-api

- build multistage
- despliegue del JAR Maven

### citabella-web

- Nginx
- sirve Angular
- redirige `/api`

---

## Variables de entorno

Permiten configurar:

- credenciales MySQL
- clave JWT
- distintos entornos

---

## Configuración Angular Routing

Nginx utiliza:

```nginx
try_files $uri /index.html;
```

Para soportar rutas SPA de Angular.

---

> **Nota:**  
> Los puertos locales y rutas exactas de `nginx.conf` deben consultarse en los archivos reales del proyecto.

---

# 8. Roadmap Técnico (Fases del Proyecto)

| Fase | Funcionalidad                                                                        | Estado     |
|------|--------------------------------------------------------------------------------------|------------|
| 1    | MVP: citas, clientes, empleados, tratamientos, JWT, Angular, Flutter WebView, Docker | Completada |
| 2    | Inventario y ventas                                                                  | Prevista   |
| 3    | Notificaciones automáticas                                                           | Prevista   |

Las futuras fases amplían el sistema sin rediseñar la arquitectura.

---

# 9. Patrones Arquitectónicos Detectados

## Backend

- Arquitectura por capas
- DTO Pattern
- Mappers estáticos
- Global Exception Handler

---

## Frontend

- Interceptor Pattern
- Guard Pattern
- Lazy Loading modular

---

## Infraestructura

- Proxy inverso con Nginx
- Contenedor WebView Flutter

---

# 10. Flujo de Despliegue General

## Pasos

1. Clonar el repositorio.

2. Iniciar Docker Desktop.

3. Ejecutar:

```bash
docker-compose up -d
```

4. Los servicios se levantan en orden:

```plaintext
db → api → web
```

5. La aplicación quedará accesible desde el puerto expuesto por Nginx.

---

> **Importante:**  
> Los puertos definitivos y comandos exactos deben verificarse en:
>
> - `docker-compose.yml`
> - `nginx.conf`

---