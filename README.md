# CitaBella — Sistema Integral de Gestión para Peluquerías

> **CitaBella** es un proyecto desarrollado como parte del módulo de **Proyecto Intermodular** del
> **Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)**.
>
> Su objetivo es **digitalizar la gestión integral de una peluquería real**, sustituyendo los procesos
> manuales (agenda en papel y WhatsApp) por una solución moderna, segura y escalable, basada en una
> **arquitectura contenerizada**.

---

## ✅ Estado actual del proyecto — Fase 1 completada

| Componente | Estado |
|---|---|
| 🧩 Análisis y diseño del sistema | ✅ Completado |
| ⚙️ Backend API REST (Spring Boot + JWT) | ✅ Completado |
| 💻 Frontend web (Angular) | ✅ Completado |
| 📱 Aplicación móvil (Flutter WebView) | 🚧 En progreso |
| 🐳 Arquitectura Docker Compose integrada | 🚧 En progreso |
| 📝 Documentación técnica (memoria) | 🚧 En progreso |
| 📈 Gestión de ventas e inventario (Fase 2) | 🔜 Planificado |
| 🔔 Notificaciones automáticas (Fase 3) | 🔜 Planificado |

El MVP de la Fase 1 incluye: gestión completa de **citas, clientes, empleados y tratamientos**,
API REST documentada con Swagger, zona pública informativa, panel privado con control de acceso
por roles, soporte para modo oscuro/claro y aplicación móvil funcional.

---

## Tecnologías utilizadas

| Componente | Tecnología |
|---|---|
| Lenguaje backend | Java 17 |
| Framework backend | Spring Boot 3.x |
| Seguridad | Spring Security + JWT (HS256) |
| Base de datos | MySQL 8 |
| ORM | JPA / Hibernate (Spring Data) |
| Frontend web | Angular 18 |
| Aplicación móvil | Flutter SDK (WebView) |
| Proxy inverso | Nginx |
| Contenerización | Docker / Docker Compose |
| Documentación API | Springdoc OpenAPI (Swagger UI) |
| IDEs | IntelliJ IDEA Ultimate, VS Code |
| Control de versiones | Git / GitHub |
| Gestión de tareas | GitHub Projects |

---

## Arquitectura del sistema

El sistema sigue una **arquitectura cliente-servidor contenerizada**, con un único punto de entrada externo:

```
                    ┌─────────────────────────────────────────┐
                    │              Docker Compose              │
                    │                                          │
  Navegador / App ──┤──► Nginx (proxy inverso) ──► Angular    │
                    │           │                              │
                    │           └──► /api ──► Spring Boot      │
                    │                              │            │
                    │                           MySQL 8         │
                    └─────────────────────────────────────────┘
```

- El **backend y la base de datos no exponen puertos al exterior**
- Toda la comunicación externa pasa a través de **Nginx** como proxy inverso
- La autenticación es **stateless mediante tokens JWT** (HS256)
- El control de acceso se aplica a dos niveles: configuración global (SecurityConfig) y por método (@PreAuthorize)

---

## Roles del sistema

| Rol | Capacidades |
|---|---|
| `ADMIN` | Acceso completo: citas, clientes, empleados, tratamientos, usuarios y roles |
| `EMPLOYEE` | Gestión operativa: consultar y crear citas, registrar y consultar clientes |
| `CLIENT` | Consulta de sus propias citas desde el panel privado |
| Visitante | Zona pública: página de inicio, servicios y catálogo de productos (sin autenticación) |

---

## 📁 Estructura del repositorio

```
CitaBella/
│
├── app/                    → Aplicación móvil (Flutter WebView)
├── backend/                → API REST (Spring Boot)
│   └── CitaBellaAPI/
│       └── Dockerfile
├── db/                     → Scripts y configuración de base de datos
├── frontend/               → Aplicación web (Angular)
├── infra/                  → Configuración de infraestructura (variables de entorno)
├── nginx/                  → Configuración del servidor web y proxy inverso
│   └── nginx.conf
├── docs/                   → Documentación, diagramas y capturas del sistema
│   ├── capturas/
│   └── diagramas/
│
├── .gitignore
├── LICENSE
├── README.md
└── docker-compose.yml      → Orquestación completa del sistema
```

---

## 🔀 Repositorio GitHub

El proyecto utiliza una estrategia de **monorepo**, integrando todos los componentes del sistema dentro de un único repositorio.

### Estructura de ramas

| Rama | Propósito |
|---|---|
| `main` | Versión estable del sistema |
| `dev` | Rama de integración principal |
| `dev-backend` | Desarrollo de la API REST (Spring Boot) |
| `dev-frontend` | Desarrollo de la interfaz web (Angular) |
| `dev-app` | Desarrollo de la aplicación móvil (Flutter WebView) |
| `docs` | Documentación y memoria técnica |

### Flujo de trabajo

```
dev-backend ──┐
dev-frontend ──┼──► dev ──► main
dev-app ───────┘
docs ──────────┘
```

1. Desarrollo de funcionalidades en ramas específicas (`dev-backend`, `dev-frontend`, `dev-app`)
2. Validación local de cada módulo
3. Integración progresiva en la rama `dev`
4. Resolución de conflictos y pruebas conjuntas
5. Merge a `main` para versiones estables

Se han utilizado archivos `.gitkeep` para mantener la estructura de carpetas en el repositorio antes de la integración de cada módulo.

### Gestión de tareas

La planificación y seguimiento se ha realizado mediante **GitHub Projects**, organizando las tareas en estados (pendiente, en desarrollo, completada), lo que ha permitido mantener una planificación incremental alineada con las fases del proyecto.

---

## 🐳 Despliegue con Docker

El sistema completo puede levantarse con un único comando:

```bash
docker compose up
```

Esto inicia tres servicios en orden de dependencia:

1. `citabella-db` — Base de datos MySQL 8 (con volumen persistente)
2. `citabella-api` — Backend Spring Boot (construido con Dockerfile multistage)
3. `citabella-web` — Nginx sirviendo el frontend Angular compilado y actuando como proxy inverso hacia `/api`

La configuración sensible (credenciales de BD, clave JWT, tiempo de expiración) se gestiona mediante variables de entorno en `docker-compose.yml`, sin modificar el código fuente.

---

## 🚀 Instalación y ejecución

### Opción 1 — Entorno completo con Docker (recomendado)

```bash
# Clonar el repositorio
git clone https://github.com/pepolyente/2526_IVAN_LOPEZ_MOLINA_CITABELLA.git
cd 2526_IVAN_LOPEZ_MOLINA_CITABELLA

# Levantar todos los servicios
docker compose up
```

El sistema estará disponible en `http://localhost`.
La documentación interactiva de la API estará disponible en `http://localhost/swagger-ui.html`.

### Opción 2 — Frontend en modo desarrollo

```bash
cd frontend
npm install
ng serve
# Disponible en http://localhost:4200
```

> El proxy de Angular redirige automáticamente las peticiones `/api` hacia el backend.

### Opción 3 — Aplicación móvil (Flutter)

```bash
cd app
flutter pub get
flutter run
```

> La app carga la versión web del sistema mediante WebView, reutilizando completamente la interfaz Angular.

---

## 🗓️ Fases del proyecto

| Fase | Periodo | Contenido |
|---|---|---|
| Fase 1 | Oct 2025 – Abr 2026 | Análisis, diseño, backend, frontend, app móvil, Docker, documentación |
| Fase 2 | Planificado | Gestión de ventas, inventario y control de stock |
| Fase 3 | Planificado | Notificaciones automáticas por email y WhatsApp |

> El modelo de datos y la arquitectura están diseñados desde la Fase 1 para soportar las ampliaciones futuras sin reestructuraciones.

---

## 👤 Autor

**Iván López Molina**

- 🎓 Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)
- 🏫 IES La Vereda — Tutor: Francisco Hernández
- 📅 Curso 2025–2026
- 📍 La Pobla de Vallbona, Valencia

🔗 [LinkedIn](https://www.linkedin.com/in/ivan-lopez-molina) · [GitHub](https://github.com/pepolyente)

---

## 🪪 Licencia

Copyright © 2025–2026 Iván López Molina. Todos los derechos reservados.

Este código fuente y sus archivos asociados están protegidos por derechos de autor. No se permite su uso, reproducción, modificación, distribución o publicación total o parcial sin el consentimiento expreso del autor.

> ⚠️ Este repositorio es privado y corresponde a un proyecto académico en curso.
