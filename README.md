#  CitaBella — Sistema Integral de Gestión para Peluquerías

> **Proyecto académico | Módulo de Proyecto Intermodular**
> **CFGS Desarrollo de Aplicaciones Multiplataforma (DAM) — Curso 2025–2026**

[![Estado MVP](https://img.shields.io/badge/Fase%201%20MVP-100%25%20completado-success?style=for-the-badge&logo=github)](#)
[![Docker Compose](https://img.shields.io/badge/despliegue-Docker%20Compose-blue?style=for-the-badge&logo=docker)](#)
[![Java 17](https://img.shields.io/badge/backend-Java%2017%20%2B%20Spring%20Boot%203-6DB33F?style=for-the-badge&logo=spring)](#)
[![Angular 21](https://img.shields.io/badge/frontend-Angular%2021-DD0031?style=for-the-badge&logo=angular)](#)
[![Flutter](https://img.shields.io/badge/m%C3%B3vil-Flutter%20%2B%20WebView-02569B?style=for-the-badge&logo=flutter)](#)

---

##  Visión general

**CitaBella** digitaliza por completo la operativa diaria de una peluquería o centro de estética. Sustituye las agendas de papel, las confirmaciones manuales por WhatsApp y las hojas de cálculo dispersas por un ecosistema de software moderno, seguro y desplegable con un solo comando.

La **Fase 1 (MVP)** está completamente cerrada, probada y lista para evaluación. Cubre el ciclo de vida completo de una cita —desde el registro del cliente hasta el cierre del servicio— con control de acceso por roles, interfaz web SPA, aplicación móvil nativa y una arquitectura de contenedores lista para producción académica.

<p align="center">
  <img src="docs/diagrams/Diagrama de Contexto del Sistema.png" alt="Diagrama de Contexto del Sistema" width="600">
</p>

---

## 🏗 Arquitectura del sistema

El sistema sigue una arquitectura **cliente‑servidor contenerizada** con un único punto de entrada. Solo el proxy inverso Nginx está expuesto al exterior; el backend Spring Boot y la base de datos MySQL viven en una red interna aislada (`app-net`).

<p align="center">
  <img src="docs/diagrams/Arquitectura del Sistema — Docker Compose.png" alt="Arquitectura Docker Compose" width="750">
</p>

La autenticación es **stateless mediante JWT (HS256)**. Tras el login, el token se almacena en el cliente y viaja en cada petición dentro de un interceptor HTTP. El backend valida la firma y los roles en cada endpoint sin mantener sesión en servidor.

###  Flujo de autenticación

1. **Login** → el servidor genera un JWT firmado.
2. El JWT se guarda en `localStorage` (web) o en el almacenamiento del WebView (móvil).
3. Un **interceptor** añade automáticamente `Authorization: Bearer <token>` a todas las peticiones.
4. El filtro `JwtAuthFilter` valida el token, extrae el rol y lo inyecta en el `SecurityContextHolder`.

| Diagrama de secuencia | Enlace |
|-----------------------|--------|
| Login JWT | [UML - Login JWT.png](docs/diagrams/UML%20-%20Login%20JWT.png) |
| Petición autenticada | [UML - Peticion Autenticada JWT.png](docs/diagrams/UML%20-%20Peticion%20Autenticada%20JWT.png) |
| JWT expirado/inválido | [UML - JWT Expirado o Invalido.png](docs/diagrams/UML%20-%20JWT%20Expirado%20o%20Invalido.png) |

---

##  Stack tecnológico

| Componente | Tecnología |
| :--- | :--- |
| Lenguaje backend | Java 17 |
| Framework backend | Spring Boot 3.x (Maven) |
| Seguridad | Spring Security + JWT (HS256) |
| Base de datos | MySQL 8 |
| ORM | JPA / Hibernate (Spring Data) |
| Frontend web | Angular 21 (SPA, NgModules) |
| Aplicación móvil | Flutter ≥3.3 + InAppWebView |
| Proxy inverso | Nginx (Alpine) |
| Contenerización | Docker + Docker Compose |
| Documentación API | Springdoc OpenAPI (Swagger UI) |
| Gestión de proyecto | GitHub Projects + monorepo Git |

---

##  Pilares técnicos del MVP

### ⚙️ Backend — API REST profesional

| Característica | Descripción |
|---------------|-------------|
| **Arquitectura por capas** | Controladores → Servicios → Repositorios → Entidades JPA. Separación estricta de responsabilidades. |
| **Seguridad sin estado** | JWT HS256 + filtro `OncePerRequestFilter`. Roles `ADMIN`, `EMPLOYEE`, `CLIENT` y `USER`. Endpoints protegidos con `@PreAuthorize`. |
| **Máquina de estados de citas** | `PENDING → CONFIRMED → IN_PROGRESS → COMPLETED`. Transiciones con cancelación y `NO_SHOW`. Detección automática de solapamientos mediante JPQL. |
| **Borrado lógico** | Todos los registros se desactivan (`active = false`). Los usuarios se bloquean (`LOCKED`). Trazabilidad total. |
| **DTOs inmutables** | Uso de `record` de Java + validación Jakarta (`@NotBlank`, `@Email`, etc.). Respuestas paginadas uniformes con `PageResponse<T>`. |
| **Manejo global de excepciones** | `GlobalExceptionHandler` que traduce cada excepción (404, 400, 403, 500) a un JSON estructurado. |
| **Inicializador de datos demo** | `DemoDataInitializer` puebla automáticamente roles, administrador, empleados, clientes, tratamientos y productos si la BD está vacía y `app.demo.enabled=true`. |

<p align="center">
  <img src="docs/diagrams/ERD Chen - Usuarios, Clientes y Seguridad.png" alt="ERD Usuarios, Clientes y Seguridad" width="650">
  <br>
  <em>Modelo entidad-relación del núcleo de seguridad y clientes.</em>
</p>

### 💻 Frontend Web — SPA en Angular 21

| Característica | Descripción |
|---------------|-------------|
| **Lazy loading por módulos** | Cada área funcional (citas, clientes, empleados, etc.) se carga bajo demanda. Rutas protegidas por `AuthGuard` y `RoleGuard`. |
| **Interceptor JWT automático** | Adjunta el token a todas las peticiones salientes. Maneja errores 401 y 403 redirigiendo al login o a la home. |
| **Calendario interactivo** | Integración con FullCalendar (vistas mes/semana/día). Creación de citas mediante un asistente de 3 pasos. Colores por estado e indicador de solapamiento (⚠️). |
| **Diseño responsivo con tokens CSS** | Más de 60 variables CSS (colores, sombras, radios). Tema claro (Navy) y oscuro (Gold) con selector de tema persistente. |
| **Edición inline y formularios reactivos** | Tablas paginadas con búsqueda, ordenación y edición en línea para clientes, empleados, tratamientos y productos. |
| **Zona pública** | Home, catálogo de servicios y productos accesibles sin autenticación. Efectos de glassmorfismo, skeleton loading y diseño atractivo. |

### 📱 Aplicación Móvil — Shell nativo Flutter + WebView

| Característica | Descripción |
|---------------|-------------|
| **WebView avanzado** | Utiliza `flutter_inappwebview` (no `webview_flutter`) para embeber la SPA Angular con soporte completo de JavaScript, DOM Storage e IndexedDB. |
| **Experiencia nativa** | Splash screen nativo (API Android 12+), pantalla de error sin conexión, pull-to-refresh y gestión del botón Atrás con diálogo de salida. |
| **Sin duplicar código** | Toda la lógica de negocio, autenticación e interfaz reside en la SPA Angular. Flutter solo gestiona la capa de presentación nativa y la conectividad. |
| **Configuración por entorno** | URL del servidor centralizada en `app_config.dart`. Compatible con emulador Android, dispositivo físico en red local y despliegue productivo con Nginx. |

### 🐳 DevOps e Infraestructura — Docker Compose

| Característica | Descripción |
|---------------|-------------|
| **Tres servicios contenerizados** | `mysql` (MySQL 8), `backend` (Spring Boot + Maven multistage), `nginx` (Angular + proxy inverso). Solo Nginx expone el puerto 80. |
| **Healthchecks y arranque ordenado** | El backend espera a que MySQL esté listo con `wait-for-it.sh`. Nginx solo arranca cuando el backend supera el healthcheck de Spring Actuator (`service_healthy`). |
| **Dockerfiles multietapa** | Backend compila con Maven y empaqueta solo el JAR en una imagen ligera de `eclipse-temurin`. Frontend compila con Node 22 y sirve los estáticos con Nginx Alpine. |
| **Persistencia de datos** | Volumen Docker `mysql_data` para conservar la BD entre recreaciones de contenedores. Backend and Nginx son completamente stateless. |
| **Variables de entorno centralizadas** | Archivo `.env` para credenciales, JWT, administrador inicial. El perfil Docker de Spring Boot activa `application-docker.properties`. |

---

## 📚 Documentación avanzada

Cada módulo del sistema cuenta con su propia guía técnica detallada dentro del directorio `docs/`.

| Documento | Contenido |
|-----------|---|
| [`docs/Backend.md`](docs/Backend.md) | API REST, paquetes, seguridad JWT, capas de servicio, endpoints, modelo de dominio, enums y buenas prácticas. |
| [`docs/Frontend.md`](docs/Frontend.md) | Módulos Angular con lazy loading, guards, interceptores, componentes por zona, calendario, diseño visual, tokens CSS y configuración. |
| [`docs/DevOps.md`](docs/DevOps.md) | Docker Compose, Dockerfiles multietapa, Nginx, healthchecks, persistencia, variables de entorno y checklist de producción. |
| [`docs/App-Android-IOS.md`](docs/App-Android-IOS.md) | App Flutter WebView, flujo de arranque, estados de conectividad, configuración del WebView, permisos y compilación. |
| [`docs/Historias_de_usuario.md`](docs/Historias_de_usuario.md) | Definición completa de los requisitos funcionales del sistema expresados como historias de usuario. |
| [`docs/api-docs.html`](docs/api-docs.html) | **Documentación interactiva de la API REST.** Página estática autogenerada con Springdoc OpenAPI que permite explorar y probar visualmente todos los endpoints sin compilar ni levantar el servidor. El tribunal puede auditar la API completa con un solo clic. |

Los diagramas de arquitectura, entidad‑relación y secuencia UML están disponibles en la carpeta [`docs/diagrams/`](docs/diagrams/).

---

## 🚀 Despliegue rápido

### Requisitos previos
- Docker Desktop (o Docker Engine + Docker Compose v2)
- Archivo `.env` configurado a partir de la plantilla

```bash
cp .env.example .env
# Editar .env con los valores reales

```

### Entorno completo (recomendado)

```bash
git clone [https://github.com/pepolyente/2526_IVAN_LOPEZ_MOLINA_CITABELLA.git](https://github.com/pepolyente/2526_IVAN_LOPEZ_MOLINA_CITABELLA.git)
cd 2526_IVAN_LOPEZ_MOLINA_CITABELLA
docker compose up -d

```

La aplicación estará disponible en `http://localhost`.

La documentación interactiva Swagger UI en `http://localhost/swagger-ui.html`.

> 💡 **Usuarios de prueba**
> Tras el primer arranque (con `app.demo.enabled=true` por defecto), el inicializador crea automáticamente los siguientes accesos:
> * **Administrador:** `citabella` / `citabella123`
> * Clientes, empleados y usuarios adicionales listados en la documentación del backend.
>
>

---

## 🛣️ Roadmap / Futuras líneas de trabajo (Fase 2 y 3)

El modelo de datos y la arquitectura se diseñaron desde la Fase 1 para absorber las siguientes ampliaciones sin reestructuraciones. Las entidades y repositorios ya están definidos en el código base.

| Fase | Estado | Contenido |
| --- | --- | --- |
| **Fase 2** | Planificado | Gestión de ventas (TPV), inventario y control de stock en tiempo real. |
| **Fase 3** | Planificado | Notificaciones automáticas por email, push (FCM) y WhatsApp. Recordatorios de cita, felicitaciones de cumpleaños y promociones. |

---

## 👤 Autor

**Iván López Molina**

* 🎓 CFGS Desarrollo de Aplicaciones Multiplataforma (DAM)
* 🏫 IES La Vereda — Tutor: Francisco Hernández
* 📅 Curso 2025–2026
* 📍 La Pobla de Vallbona, Valencia

🔗 [LinkedIn](https://www.linkedin.com/in/ivan-lopez-molina) · [GitHub](https://github.com/pepolyente)

---

## 🪪 Licencia

Copyright © 2025–2026 Iván López Molina. Todos los derechos reservados.

Este código fuente y sus archivos asociados están protegidos por derechos de autor. No se permite su uso, reproducción, modificación, distribución o publicación total o parcial sin el consentimiento expreso del autor.
