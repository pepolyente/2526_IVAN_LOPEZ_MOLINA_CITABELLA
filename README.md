# CitaBella — Sistema Integral de Gestión para Peluquerías

> **CitaBella** es un proyecto desarrollado como parte del módulo de **Proyecto Intermodular** del
> **Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)**.

---

## ¿Qué es CitaBella y qué problema resuelve?

Muchas peluquerías y pequeños negocios de estética siguen gestionando su día a día con medios que no escalan: una agenda
de papel para las citas, el móvil personal para confirmar reservas por WhatsApp y hojas de cálculo para llevar un
control básico del negocio. Este modelo manual es propenso a errores, genera duplicidades, no ofrece visibilidad en
tiempo real y limita la capacidad de crecer.

**CitaBella nace para digitalizar ese flujo de trabajo completo.** El objetivo es sustituir los procesos manuales por
una solución moderna, segura y escalable, desarrollada específicamente para el contexto de una peluquería real, con sus
empleados, sus tratamientos y sus clientes.

El sistema cubre el ciclo completo de una cita: desde que un cliente se registra o es dado de alta por el personal,
pasando por la reserva y confirmación, hasta el cierre del servicio. Todo ello con control de acceso por roles,
historial de estados y una interfaz clara tanto en escritorio como en móvil.

---

## ✅ Estado actual — Fase 1 completada (MVP)

| Componente                                 | Estado         |
|--------------------------------------------|----------------|
| 🧩 Análisis y diseño del sistema           | ✅ Completado   |
| ⚙️ Backend API REST (Spring Boot + JWT)    | ✅ Completado   |
| 💻 Frontend web (Angular 18)               | ✅ Completado   |
| 📱 Aplicación móvil (Flutter WebView)      | ✅ Completado   |
| 🐳 Arquitectura Docker Compose integrada   | ✅ Completado   |
| 📝 Documentación técnica (memoria)         | ✅ Completado   |
| 📈 Gestión de ventas e inventario (Fase 2) | 🔜 Planificado |
| 🔔 Notificaciones automáticas (Fase 3)     | 🔜 Planificado |

---

## Qué ofrece la Fase 1 (MVP)

La Fase 1 constituye el núcleo operativo del sistema. No es una demo ni un prototipo: es una aplicación funcional y
desplegable que cubre los procesos cotidianos de una peluquería de principio a fin.

### Gestión de citas

El módulo central del sistema. Permite crear, visualizar, modificar y cancelar citas con una interfaz de calendario
integrada (vistas por mes, semana y día). Cada cita atraviesa una máquina de estados controlada:

```
PENDING → CONFIRMED → IN_PROGRESS → COMPLETED
                   ↘ CANCELLED / NO_SHOW
```

El sistema detecta automáticamente solapamientos de citas para el mismo empleado y los advierte visualmente. La creación
de citas sigue un asistente de tres pasos: selección de cliente y horario, elección de tratamientos y empleado, y
confirmación con resumen.

### Gestión de clientes y empleados

Alta, edición y desactivación de clientes y empleados con borrado lógico (los registros nunca se eliminan físicamente,
garantizando trazabilidad). Cada cliente o empleado puede vincularse a una cuenta de usuario del sistema, activando
automáticamente el rol y permisos correspondientes.

### Catálogo de tratamientos y productos

Gestión completa del catálogo de servicios (con duración mínima/máxima y precio) y del inventario de productos (con
precio de compra, precio de venta, tipo de uso y proveedor). La zona pública del sistema muestra estos catálogos sin
necesidad de autenticación.

### Zona pública informativa

Página de inicio, listado de servicios y catálogo de productos accesibles para cualquier visitante, sin necesidad de
cuenta. Pensada para ser el escaparate digital del negocio.

### Control de acceso por roles

Cuatro roles con permisos diferenciados: `ADMIN`, `EMPLOYEE`, `CLIENT` y `USER`. El acceso a cada sección del panel
privado está protegido tanto a nivel de enrutamiento (frontend) como a nivel de endpoint (backend), sin posibilidad de
saltarse las restricciones desde el cliente.

| Rol        | Capacidades                                                                                |
|------------|--------------------------------------------------------------------------------------------|
| `ADMIN`    | Acceso completo: citas, clientes, empleados, tratamientos, productos y gestión de usuarios |
| `EMPLOYEE` | Gestión operativa: crear y consultar citas, registrar y consultar clientes                 |
| `CLIENT`   | Consulta de sus propias citas desde el panel privado                                       |
| Visitante  | Zona pública: inicio, servicios y productos (sin autenticación)                            |

### Aplicación móvil funcional

Una app Android nativa (Flutter) que empaqueta la interfaz web Angular mediante WebView avanzado, ofreciendo experiencia
móvil completa desde el primer día sin duplicar código ni lógica de negocio.

### Despliegue con un solo comando

Toda la infraestructura (base de datos, backend y frontend) arranca con `docker compose up`. No se requiere
configuración manual de servicios ni instalación de dependencias en el servidor.

---

## Arquitectura del sistema

El sistema sigue una **arquitectura cliente-servidor completamente contenerizada**, con un único punto de entrada
externo. El backend y la base de datos nunca están expuestos directamente: toda la comunicación pasa por Nginx.

```
                    ┌─────────────────────────────────────────┐
                    │              Docker Compose              │
                    │                                          │
  Navegador / App ──┼──► Nginx (:80) ──► Angular (SPA)        │
                    │        │                                 │
                    │        └──► /api ──► Spring Boot (:8080) │
                    │                          │               │
                    │                       MySQL 8 (:3306)    │
                    └─────────────────────────────────────────┘
```

La autenticación es **stateless mediante JWT** (HS256). Una vez que el usuario inicia sesión, el token se almacena en
`localStorage` y se adjunta automáticamente a todas las peticiones posteriores mediante un interceptor HTTP en Angular.
El backend valida el token en cada petición sin necesidad de mantener sesión en servidor.

---

## Stack tecnológico

| Componente           | Tecnología                     |
|----------------------|--------------------------------|
| Lenguaje backend     | Java 17                        |
| Framework backend    | Spring Boot 3.x                |
| Seguridad            | Spring Security + JWT (HS256)  |
| Base de datos        | MySQL 8                        |
| ORM                  | JPA / Hibernate (Spring Data)  |
| Frontend web         | Angular 18                     |
| Aplicación móvil     | Flutter SDK ≥3.3 (WebView)     |
| Proxy inverso        | Nginx                          |
| Contenerización      | Docker / Docker Compose        |
| Documentación API    | Springdoc OpenAPI (Swagger UI) |
| Control de versiones | Git / GitHub                   |
| Gestión de tareas    | GitHub Projects                |

---

## 📁 Estructura del repositorio

```
CitaBella/
│
├── app/                    → Aplicación móvil (Flutter WebView)
├── backend/                → API REST (Spring Boot + Dockerfile multistage)
│   └── CitaBellaAPI/
├── db/                     → Scripts y configuración de base de datos
├── frontend/               → Aplicación web (Angular 18 + Dockerfile multistage)
│   └── citabella-web/
├── nginx/                  → Configuración del proxy inverso
│   └── nginx.conf
├── docs/                   → Documentación técnica
│   ├── Backend.md          → API REST, entidades, seguridad y endpoints
│   ├── Frontend.md         → Angular, módulos, enrutamiento y diseño
│   ├── DevOps.md           → Docker Compose, Dockerfiles y despliegue
│   ├── App-Android-IOS.md  → Aplicación Flutter y configuración móvil
│   ├── capturas/
│   └── diagramas/
│
├── .env.example            → Plantilla de variables de entorno
├── docker-compose.yml      → Orquestación completa del sistema
├── LICENSE
└── README.md
```

---

## 📚 Documentación técnica

La documentación detallada de cada componente se encuentra en la carpeta `docs/`. Cada archivo describe en profundidad
la arquitectura, las decisiones de diseño y las instrucciones de uso de su módulo:

| Documento                                            | Contenido                                                                                                                                 |
|------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| [`docs/Backend.md`](docs/Backend.md)                 | Estructura de paquetes, capas (controladores, servicios, repositorios, DTOs), seguridad JWT, endpoints, modelo de dominio y enums         |
| [`docs/Frontend.md`](docs/Frontend.md)               | Módulos Angular con lazy loading, sistema de enrutamiento, guards, interceptores, componentes por zona y sistema de diseño con tokens CSS |
| [`docs/DevOps.md`](docs/DevOps.md)                   | Dockerfiles multistage, Docker Compose, variables de entorno, Nginx como proxy inverso, healthchecks y checklist de producción            |
| [`docs/App-Android-IOS.md`](docs/App-Android-IOS.md) | Arquitectura Flutter WebView, flujo de arranque, estados del WebView, conectividad, configuración por entorno y compilación               |

La memoria completa del proyecto está disponible en [`docs/Memoria.pdf`](docs/Memoria.pdf).

---

## 🚀 Instalación y ejecución

### Requisitos previos

- Docker Desktop (o Docker Engine + Compose v2)
- Fichero `.env` configurado a partir de la plantilla:

```bash
cp .env.example .env
# Editar .env con los valores reales
```

### Opción 1 — Entorno completo con Docker (recomendado)

```bash
git clone https://github.com/pepolyente/2526_IVAN_LOPEZ_MOLINA_CITABELLA.git
cd 2526_IVAN_LOPEZ_MOLINA_CITABELLA
docker compose up -d
```

La aplicación estará disponible en `http://localhost`.
La documentación interactiva de la API (Swagger UI) estará en `http://localhost/swagger-ui.html`.

Para reconstruir tras cambios en el código:

```bash
docker compose up -d --build
```

### Opción 2 — Frontend en modo desarrollo

```bash
cd frontend/citabella-web
npm install
ng serve        # Disponible en http://localhost:4200
```

El proxy de Angular redirige automáticamente `/api` hacia el backend en `localhost:8080`.

### Opción 3 — Aplicación móvil (Flutter)

```bash
cd app
flutter pub get
# Ajustar baseUrl en lib/core/config/app_config.dart según el entorno
flutter run
```

La app carga la interfaz Angular mediante WebView. Ver [`docs/App-Android-IOS.md`](docs/App-Android-IOS.md) para la
configuración de URLs por entorno (emulador, dispositivo físico, producción).

---

## 🔀 Repositorio y flujo de trabajo

El proyecto utiliza una estrategia de **monorepo** con ramas separadas por módulo:

| Rama           | Propósito                         |
|----------------|-----------------------------------|
| `main`         | Versión estable del sistema       |
| `dev`          | Rama de integración principal     |
| `dev-backend`  | Desarrollo de la API REST         |
| `dev-frontend` | Desarrollo de la interfaz web     |
| `dev-app`      | Desarrollo de la aplicación móvil |
| `docs`         | Documentación y memoria técnica   |

### Flujo de integración

```
dev-backend ──┐
dev-frontend ──┼──► dev ──► main
dev-app ───────┘
docs ──────────┘
```

La planificación y el seguimiento de tareas se han gestionado con **GitHub Projects**, organizando el trabajo en
estados (pendiente, en desarrollo, completada) de forma incremental y alineada con las fases del proyecto.

---

## 🗓️ Fases del proyecto

El modelo de datos y la arquitectura están diseñados desde la Fase 1 para soportar las ampliaciones futuras sin
reestructuraciones. Los dominios de ventas, stock, notificaciones y auditoría ya tienen entidades y repositorios
definidos en el backend, pendientes de implementar en las siguientes fases.

| Fase          | Periodo             | Contenido                                                             |
|---------------|---------------------|-----------------------------------------------------------------------|
| **Fase 1** ✅  | Oct 2025 – Abr 2026 | Análisis, diseño, backend, frontend, app móvil, Docker, documentación |
| **Fase 2** 🔜 | Planificado         | Gestión de ventas, inventario y control de stock                      |
| **Fase 3** 🔜 | Planificado         | Notificaciones automáticas por email, push y WhatsApp                 |

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